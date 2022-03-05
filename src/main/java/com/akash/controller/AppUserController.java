package com.akash.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akash.entity.AppUser;
import com.akash.entity.AppUserSearch;
import com.akash.repository.AppUserRepository;
import com.akash.repository.LabourGroupRepository;
import com.akash.repository.SiteRepository;
import com.akash.repository.UserTypeRepository;
import com.akash.util.Constants;

@Controller
@RequestMapping("/user")
public class AppUserController {
	@Autowired
	AppUserRepository appUserRepository;
	
	@Autowired
	SiteRepository siteRepository;
	@Autowired
	UserTypeRepository userRepository;
	@Autowired
	LabourGroupRepository labourGroupRepository;
	
	int from = 0;
	int total = 0;
	int ROWS = Constants.ROWS;
	Long records = 0L;
	
	@GetMapping
	public String get(Model model)
	{
		fillModel(model);
		if (!model.asMap().containsKey("user")) {
			model.addAttribute("user", new AppUser());
		}

		if (model.asMap().containsKey("result")) {
			model.addAttribute("org.springframework.validation.BindingResult.user", model.asMap().get("result"));
		}
		return "appUser";
	}
	
	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("user") AppUser appUser,BindingResult result,Model model,RedirectAttributes redirect)
	{
		if (result.hasErrors()) {
			redirect.addFlashAttribute("user", appUser);
			redirect.addFlashAttribute("result", result);
			redirect.addFlashAttribute("fail", "Please enter all the field correctly");
			return "redirect:/user";
		}else if (appUserRepository.existsByContact(appUser.getContact()))
		{
			redirect.addFlashAttribute("user", appUser);
			redirect.addFlashAttribute("fail", "Person Already Exists");
			return "redirect:/user";
		}
		
		
		else {

			appUserRepository.save(appUser);
			redirect.addFlashAttribute("success", "Person Saved Successfully");
			return "redirect:/user";
		}
	}
	@GetMapping("/search")
	public String list(Model model,HttpSession session) {
		fillModel(model);
		model.addAttribute("appUserSearch", new AppUserSearch());
		//model.addAttribute("totalPages", 1);
		session.setAttribute("currentPage", 1);
		return "userList";
	}
	@PostMapping("/search")
	public String searchOrders(AppUserSearch appUserSearch, Model model, HttpSession session) {

		int page = 1;
		session.setAttribute("appUserSearch",appUserSearch);
		pagination(page, appUserSearch, model, session);
		return "userList";
	}
	@GetMapping("/edit/{id}")
	public String update(@PathVariable("id") long id,Model model,HttpSession session)
	{
		fillModel(model);
		if (!model.asMap().containsKey("user")) {
			model.addAttribute("user", appUserRepository.findById(id));
		}
		if (model.asMap().containsKey("result")) {
			model.addAttribute("org.springframework.validation.BindingResult.user", model.asMap().get("result"));
		}
		model.addAttribute("edit", true);
		int page = (int) session.getAttribute("currentPage");
		pagination(page, new AppUserSearch(), model, session);
		return "appUser";
	}
	
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("user") AppUser appUser,BindingResult result,RedirectAttributes redirect,HttpSession session,Model model)
	{
		
		  int page = (int) session.getAttribute("currentPage"); 
		//  pagination(page,model);
		 
		if (result.hasErrors())
		{
			redirect.addFlashAttribute("user", appUser);
			redirect.addFlashAttribute("result", result);
			redirect.addFlashAttribute("fail", "Please enter the field correctly");
			return "redirect:/user/edit/"+appUser.getId();
		}
		
		else if(appUserRepository.chechUserExistsAlready(appUser.getContact(), appUser.getId()) !=null)
		{
			redirect.addFlashAttribute("user", appUser);
			redirect.addFlashAttribute("fail", "Person Alredy Exists");
			return "redirect:/user/edit/"+appUser.getId();
		}
		else
		{
		appUserRepository.save(appUser);
			redirect.addFlashAttribute("success","Person Updated Successfully");
			
			return "redirect:/user/pageno=" + page;
		}
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id,HttpSession session,RedirectAttributes redirect)
	{
		int page= (int) session.getAttribute("currentPage");
		appUserRepository.deleteById(id);
		redirect.addFlashAttribute("success","User Deleted Successfully");
		return "redirect:/user/pageno=" +page;
	}
		
	@GetMapping("/customer")
	public ResponseEntity<?> saveCustomer(@RequestParam("name") String name,
										  @RequestParam("contact") String contact,
										  @RequestParam("address") String address,
										  @RequestParam("ledgerNumber") String ledgerNumber){
		if(appUserRepository.existsByContact(contact)) {
			return ResponseEntity.badRequest().build();
		}
		
		AppUser appUser=new AppUser();
		appUser.setName(name);
		appUser.setContact(contact);
		appUser.setAddress(address);
		appUser.setLedgerNumber(ledgerNumber);
		appUser.setUserType(userRepository.findByName(Constants.CUSTOMER));
		appUser.setActive(true);
		appUser=appUserRepository.save(appUser);
		return ResponseEntity.ok(appUser);
		
	}
	
	
	private void fillModel(Model model)
	{
		model.addAttribute("UserList", userRepository.findAll());
		model.addAttribute("siteList", siteRepository.findAll());
		model.addAttribute("labourGroups", labourGroupRepository.findAll());
	}

	public void pagination(int page, AppUserSearch appUserSearch, Model model, HttpSession session) {

		page = (page > 0) ? page : 1;
		from = ROWS * (page - 1);
		records = (long) appUserRepository.searchAppUsersCount(appUserSearch);
		total = (int) Math.ceil((double) records / (double) ROWS);
		List<AppUser> appUsers = appUserRepository.searchAppUserPaginated(appUserSearch, from);
		model.addAttribute("totalPages", total);
		session.setAttribute("currentPage", page);
		model.addAttribute("appUser",appUsers);
		model.addAttribute("appUserSearch", appUserSearch);
		System.out.println("total records: " + records + " total Pages: " + total + " Current page: " + page);
		fillModel(model);

	}
	@GetMapping("/pageno={page}")
	public String paginate(@PathVariable("page") int page, Model model, HttpSession session) {
		AppUserSearch appUserSearch =  (AppUserSearch) session.getAttribute("appUserSearch");
		
		pagination(page, appUserSearch, model, session);
		return "userList";
	}
	
	@GetMapping("/{id}/sites")
	public ResponseEntity<?> findSitesOfUser(@PathVariable long id){
		return ResponseEntity.ok(appUserRepository.findSitesOnUserId(id));
	}
	
	@GetMapping("/labour-group/{id}")
	public ResponseEntity<?> findLaboursOnLabourGroup(@PathVariable long id){
		return ResponseEntity.ok(appUserRepository.findByUserType_NameAndLabourGroup_IdAndActive(Constants.LABOUR,id,true));
	}
}
