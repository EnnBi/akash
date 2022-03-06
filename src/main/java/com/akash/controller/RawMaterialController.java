package com.akash.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akash.entity.RawMaterial;
import com.akash.entity.RawMaterialSearch;
import com.akash.repository.AppUserRepository;
import com.akash.repository.MaterialTypeRepository;
import com.akash.repository.RawMaterialRepository;
import com.akash.util.Constants;

@Controller
@RequestMapping("/raw-material")
public class RawMaterialController {
	@Autowired
	RawMaterialRepository rawMaterialRepository;
	@Autowired
	MaterialTypeRepository materialRepository;
	@Autowired
	AppUserRepository userRepository;

	int from = 0;
	int total = 0;
	int ROWS = 2;
	Long records = 0L;

	@GetMapping
	public String add(Model model) {

		fillModel(model);
		if (!model.asMap().containsKey("rawMaterial")) {
			model.addAttribute("rawMaterial", new RawMaterial());
		}

		if (model.asMap().containsKey("result")) {
			model.addAttribute("org.springframework.validation.BindingResult.rawMaterial", model.asMap().get("result"));
		}
		return "rawMaterial";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("rawMaterial") RawMaterial rawMaterial, BindingResult result, Model model,
			RedirectAttributes redirect) {
		
		if(Objects.isNull(rawMaterial.getDate())){
			rawMaterial.setDate(LocalDate.now());
		}

		if (result.hasErrors()) {
			redirect.addFlashAttribute("rawMaterial", rawMaterial);
			redirect.addFlashAttribute("result", result);
			redirect.addFlashAttribute("fail", "Please enter all the field correctly");
			return "redirect:/raw-material";
		}

		else if (rawMaterialRepository.existsByChalanNumber(rawMaterial.getChalanNumber())) {
			redirect.addFlashAttribute("rawMaterial", rawMaterial);
			redirect.addFlashAttribute("result", result);
			redirect.addFlashAttribute("fail", "Raw Material Already Exists");
			return "redirect:/raw-material";
		}

		else {

			
			rawMaterialRepository.save(rawMaterial);
			redirect.addFlashAttribute("success", "Raw Material Saved Successfully");
			return "redirect:/raw-material";
		}
	}

	@GetMapping("/search")
	public String list(Model model, HttpSession session) {
		fillModel(model);
		model.addAttribute("rawMaterialSearch", new RawMaterialSearch());
		// model.addAttribute("totalPages", 1);
		session.setAttribute("currentPage", 1);

		return "rawList";
	}

	@PostMapping("/search")
	public String searchOrders(RawMaterialSearch rawMaterialSearch, Model model, HttpSession session) {

		int page = 1;
		session.setAttribute("rawMaterialSearch", rawMaterialSearch);
		pagination(page, rawMaterialSearch, model, session);
		return "rawList";
	}

	@GetMapping("/edit/{id}")
	public String update(@PathVariable("id") long id, Model model, HttpSession session) {
		fillModel(model);
		if (!model.asMap().containsKey("rawMaterial")) {
			model.addAttribute("rawMaterial", rawMaterialRepository.findById(id));
		}
		if (model.asMap().containsKey("result")) {
			model.addAttribute("org.springframework.validation.BindingResult.rawMaterial", model.asMap().get("result"));
		}
		model.addAttribute("edit", true);
		int page = (int) session.getAttribute("currentPage");
		// pagination(page, model);
		return "rawMaterial";
	}

	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("rawMaterial") RawMaterial rawMaterial, BindingResult result,
			RedirectAttributes redirect, HttpSession session, Model model) {
		
		if(Objects.isNull(rawMaterial.getDate())){
			rawMaterial.setDate(LocalDate.now());
		}

		int page = (int) session.getAttribute("currentPage");
		// pagination(page,model);

		if (result.hasErrors()) {
			redirect.addFlashAttribute("rawMaterial", rawMaterial);
			redirect.addFlashAttribute("result", result);
			redirect.addFlashAttribute("fail", "Please enter all the fields correctly");
			return "redirect:/raw-material/edit/" + rawMaterial.getId();
		}

		else if (rawMaterialRepository.checkRawMaterialAlreadyExists(rawMaterial.getChalanNumber(),
				rawMaterial.getId()) != null) {
			redirect.addFlashAttribute("rawMaterial", rawMaterial);
			redirect.addFlashAttribute("fail", "Raw Material Already Exists");
			return "redirect:/raw-material/edit/" + rawMaterial.getId();
		} else {
			rawMaterialRepository.save(rawMaterial);
			redirect.addFlashAttribute("success", "Raw Material Updated Successfully");

			return "redirect:/raw-material/" + page;
		}
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id, HttpSession session, RedirectAttributes redirect) {

		int page = (int) session.getAttribute("currentPage");
		rawMaterialRepository.deleteById(id);
		redirect.addFlashAttribute("success", "Raw Material Deleted Successfully");
		return "redirect:/raw-material/" + page;
	}

	private void fillModel(Model model) {
		model.addAttribute("userList", userRepository.findByUserType_NameAndActive(Constants.DEALER,true));
		model.addAttribute("rawList", materialRepository.findAll());
	}

	public void pagination(int page, RawMaterialSearch rawMaterialSearch, Model model, HttpSession session) {

		page = (page > 0) ? page : 1;
		from = ROWS * (page - 1);
		records = (long) rawMaterialRepository.searchRawMaterialsCount(rawMaterialSearch);
		total = (int) Math.ceil((double) records / (double) ROWS);
		List<RawMaterial> rawMaterials = rawMaterialRepository.searchRawMaterialPaginated(rawMaterialSearch, from);
		model.addAttribute("totalPages", total);
		session.setAttribute("currentPage", page);
		model.addAttribute("rawMaterial", rawMaterials);
		model.addAttribute("rawMaterialSearch", rawMaterialSearch);
		System.out.println("total records: " + records + " total Pages: " + total + " Current page: " + page);
		fillModel(model);

	}

	@GetMapping("/{page}")
	public String showRawMaterial(@PathVariable("page") int page, Model model, HttpSession session) {
		RawMaterialSearch rawMaterialSearch = (RawMaterialSearch) session.getAttribute("rawMaterialSearch");

		pagination(page, rawMaterialSearch, model, session);
		return "rawList";

	}
}
