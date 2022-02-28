package com.akash.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akash.entity.ClearDues;
import com.akash.repository.AppUserRepository;
import com.akash.repository.BillBookRepository;
import com.akash.repository.ClearDuesRepository;
import com.akash.repository.DayBookRepository;
import com.akash.repository.GoodsReturnRepository;
import com.akash.repository.UserTypeRepository;
import com.akash.util.CommonMethods;
import com.akash.util.Constants;

@Controller
@RequestMapping("/clearDues")
public class ClearDuesController {
	@Autowired
	UserTypeRepository userTypeRepository;
	
	@Autowired
	ClearDuesRepository clearDuesRepository;
	
	@Autowired
	AppUserRepository appUserRepository;
	
	@Autowired
	BillBookRepository billBookRepo;
	
	@Autowired
	DayBookRepository daybookRepo;
	
	@Autowired
	GoodsReturnRepository goodsReturnRepo;
	
	@GetMapping
	public String add(Model model, HttpSession session) {
		
			model.addAttribute("clearDues", new ClearDues());
			int page =1;
			session.setAttribute("currentPage", page);
			pagination(page, model);
			
		
		return "cleardues";

	}
	
	
	@PostMapping("/save")
	public String save(@ModelAttribute("clearDues") ClearDues clearDues, Model model,RedirectAttributes redirectAttributes) {
		   clearDues.setDate(LocalDate.now());

			clearDuesRepository.save(clearDues);
			redirectAttributes.addFlashAttribute("success","ClearDues saved successfully");
		return "redirect:/clearDues";
	}
	
	
	@GetMapping("/edit/{id}")
	public String update(@PathVariable("id") long id, Model model, HttpSession session) {
		
		ClearDues clearDues=clearDuesRepository.findById(id).get();
		model.addAttribute("clearDues", clearDues);
		
		model.addAttribute("edit", true);
		
		int page = (int) session.getAttribute("currentPage");
		pagination(page, model);
		model.addAttribute("balance",CommonMethods.getCustomerBalance(clearDues.getUser().getId(),LocalDate.MIN, LocalDate.now(),billBookRepo,daybookRepo, clearDuesRepository,goodsReturnRepo));
		model.addAttribute("customers", appUserRepository.findByUserType_IdAndActive(clearDues.getUser().getUserType().getId(), true));
		return "cleardues";
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute("clearDues") ClearDues clearDues, Model model,RedirectAttributes redirectAttributes) {

			clearDuesRepository.save(clearDues);
			redirectAttributes.addFlashAttribute("success","ClearDues Updated successfully");
		return "redirect:/clearDues";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id, HttpSession session, RedirectAttributes redirect) {

		int page = (int) session.getAttribute("currentPage");
		try {
			clearDuesRepository.deleteById(id);
			redirect.addFlashAttribute("success", "ClearDues Deleted Successfully");
		} catch (Exception e) {
			redirect.addFlashAttribute("fail", "ClearDues cannot be deleted");
		}
		
		return "redirect:/clearDues/pageno=" + page;
	}
	public void pagination(int page, Model model) {

		page = page <= 1 ? 0 : page - 1;
		Pageable pageable = PageRequest.of(page, 10);
		Page<ClearDues> list = clearDuesRepository.findAll(pageable);
		System.out.println(list.getContent());
		model.addAttribute("list", list.getContent());
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("totalPages", list.getTotalPages());
		model.addAttribute("userTypes", userTypeRepository.findAll());
		model.addAttribute("owner",appUserRepository.findByUserType_NameAndActive(Constants.OWNER,true));
	}
	@GetMapping("/pageno={page}")
	public String paginate(@PathVariable("page") int page, Model model, HttpSession session) {
		session.setAttribute("currentPage", page);
		pagination(page, model);
		model.addAttribute("clearDues", new ClearDues());
		return "cleardues";
	}

}
