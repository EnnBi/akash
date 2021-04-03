package com.akash.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akash.entity.DayBook;
import com.akash.entity.DayBookSearch;
import com.akash.repository.AppUserRepository;
import com.akash.repository.DayBookRepository;
import com.akash.repository.UserTypeRepository;
import com.akash.util.Constants;

@Controller
@RequestMapping("/day-book")
public class DayBookController {

	@Autowired
	DayBookRepository daybookRepository;
	@Autowired
	UserTypeRepository userTypeRepository;
	@Autowired
	AppUserRepository appUserRepository;
	
	int from = 0;
	int total = 0;
	Long records = 0L;
	
	@GetMapping
	public String add(Model model) {
		model.addAttribute("dayBook", new DayBook());
		model.addAttribute("userTypes", userTypeRepository.findAll());
		model.addAttribute("accounts",appUserRepository.findByUserType_Name(Constants.OWNER));
		return "daybook";
	} 

	@PostMapping("/save")
	public String save(@ModelAttribute("dayBook") DayBook dayBook, Model model,RedirectAttributes redirectAttributes) {
		if(dayBook.getTransactionType().equals(Constants.EXPENDITURE) && dayBook.getTransactionBy().equals(Constants.CHEQUE)){
			DayBook prevDayBook = daybookRepository.findByTransactionNumber(dayBook.getTransactionNumber());
			prevDayBook.setStatus(Constants.SUCCESS);
			daybookRepository.save(prevDayBook);
		}
			daybookRepository.save(dayBook);
			redirectAttributes.addFlashAttribute("success","Entry saved successfully");
		return "redirect:/day-book";
	}

	@GetMapping("/edit/{id}")
	public String update(@PathVariable("id") long id, Model model) {
		model.addAttribute("dayBook", daybookRepository.findById(id).orElse(null));
		fillModel(model);
		return "daybook";
	}

	@GetMapping("/delete")
	public String delete(@PathVariable("id") long id) {
		daybookRepository.deleteById(id);
		return null;
	}

	@GetMapping("/search")
	public String searchGet(Model model) {
		model.addAttribute("dayBookSearch", new DayBookSearch());
		fillModel(model);
		return "daybookSearch";
	}
	
	@PostMapping("/search")
	public String searchPost(@ModelAttribute("dayBookSearch") DayBookSearch dayBookSearch,Model model,HttpSession session) {
		session.setAttribute("dayBookSearch",dayBookSearch);
		int page=1;
		fillModel(model);
		pagination(page, dayBookSearch, model);
		return "daybookSearch";
	}
	
	@GetMapping("/pageno={page}")
	public String page(@PathVariable("page") int page,HttpSession session,Model model){
		DayBookSearch dayBookSearch = (DayBookSearch) session.getAttribute("dayBookSearch");
		pagination(page, dayBookSearch, model);
		return "daybookSearch";
	}
	
	public void pagination(int page, DayBookSearch dayBookSearch, Model model) {
		page = (page > 0) ? page : 1;
		from = Constants.ROWS * (page - 1);
		records = (long) daybookRepository.count(dayBookSearch);
		total = (int) Math.ceil((double) records / (double) Constants.ROWS);
		List<DayBook> dayBooks = daybookRepository.searchPaginated(dayBookSearch,from);
		model.addAttribute("totalPages", total);
		model.addAttribute("currentPage", page);
		model.addAttribute("dayBooks", dayBooks);
		model.addAttribute("dayBookSearch", dayBookSearch);
		fillModel(model);

	}
	
	private void fillModel(Model model) {
		model.addAttribute("userTypes", userTypeRepository.findAll());
		model.addAttribute("accounts",appUserRepository.findByUserType_Name(Constants.OWNER));
		model.addAttribute("customers", appUserRepository.findAll());
	}
	
	
}
