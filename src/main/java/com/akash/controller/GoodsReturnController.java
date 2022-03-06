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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akash.entity.AppUser;
import com.akash.entity.BillBook;
import com.akash.entity.BillBookSearch;
import com.akash.entity.GoodsReturn;
import com.akash.entity.GoodsReturnSearch;
import com.akash.entity.Sales;
import com.akash.entity.dto.BillBookDTO;
import com.akash.repository.AppUserRepository;
import com.akash.repository.BillBookRepository;
import com.akash.repository.GoodsReturnRepository;
import com.akash.repository.LabourGroupRepository;
import com.akash.repository.ProductRepository;
import com.akash.repository.SiteRepository;
import com.akash.repository.SizeRepository;
import com.akash.repository.VehicleRepository;
import com.akash.util.Constants;

@Controller
@RequestMapping("/return-goods")
public class GoodsReturnController {
	
	int from = 0;
	int total = 0;
	Long records = 0L;
	
	@Autowired
	AppUserRepository appUserRepository;
	
	@Autowired
	BillBookRepository billBookRepository;
	@Autowired
	GoodsReturnRepository goodsReturnRepo;
	
	@Autowired
	ProductRepository productRepository;
	@Autowired
	VehicleRepository vehicleRepo;
	@Autowired
	SiteRepository siteRepository;
	@Autowired
	SizeRepository sizeRepository;
	@Autowired
	LabourGroupRepository labourGroupRepository;
	
	@GetMapping("/search")
	public String searchGet(Model model) {
		model.addAttribute("goodsReturnSearch", new BillBookSearch());
		fillModel(model);
		return "goodsreturnsearch";
	}
	
	@PostMapping("/search")
	public String searchPost(BillBookSearch billBookSearch, Model model, HttpSession session) {
		session.setAttribute("goodsReturnSearch", billBookSearch);
		int page = 1;
		pagination(page, billBookSearch, model);
		return "goodsreturnsearch";
	}
	
	
	@GetMapping("/add/{id}")
	public String update(@PathVariable("id") long id, Model model) {

		BillBook billBook = billBookRepository.findById(id).orElse(null);
		for(Sales s:billBook.getSales()) {
			s.setQuantity(null);
		}
		GoodsReturn goodsReturn= goodsReturnRepo.billBookToGoodsReturnMapping(billBook);
		model.addAttribute("goodsReturn", goodsReturn);
		String[] userTypes = { Constants.CUSTOMER, Constants.CONTRACTOR };
		model.addAttribute("customers", appUserRepository.findByUserType_NameInAndActive(userTypes, true));
		model.addAttribute("products", productRepository.findAll());
		model.addAttribute("sizes", sizeRepository.findAll());
		
		return "goodsreturnadd";
	}
	@RequestMapping(value = "/add", params = "save", method = RequestMethod.POST)
	public String update(@ModelAttribute("goodsReturn") GoodsReturn  goodsReturn, Model model,
			RedirectAttributes redirectAttributes) {
		

		 goodsReturn.getSales().forEach(s ->{
		 s.setBillBook(null);
		 s.setId(0);
		 s.setGoodsReturn(goodsReturn);
		 });
		goodsReturnRepo.save(goodsReturn);
		redirectAttributes.addFlashAttribute("success", "Goods Returned saved successfully");
		return "redirect:/return-goods/search";
	}
	
	@GetMapping("/edit")
	public String returnEditSearchGet(Model model) {
		model.addAttribute("ReturnSearch", new GoodsReturnSearch());
		fillModel(model);
		return "goodsreturneditsearch";
	}
	
	@PostMapping("/edit")
	public String searchPostEdit(GoodsReturnSearch goodsRetunSearch, Model model, HttpSession session) {
		session.setAttribute("ReturnSearch", goodsRetunSearch);
		int page = 1;
		editPagination(page, goodsRetunSearch, model);
		return "goodsreturneditsearch";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") long id, Model model) {

		GoodsReturn goodsReturn = goodsReturnRepo.findById(id).orElse(null);
		model.addAttribute("edit", true);
		model.addAttribute("goodsReturn", goodsReturn);
		String[] userTypes = { Constants.CUSTOMER, Constants.CONTRACTOR };
		model.addAttribute("customers", appUserRepository.findByUserType_NameInAndActive(userTypes, true));
		model.addAttribute("products", productRepository.findAll());
		model.addAttribute("sizes", sizeRepository.findAll());
		
		return "goodsreturnadd";
	}
	@RequestMapping(value = "/update", params = "save", method = RequestMethod.POST)
	public String updateReturnGoods(@ModelAttribute("goodsReturn") GoodsReturn  goodsReturn, Model model,
			RedirectAttributes redirectAttributes) {
		

		 goodsReturn.getSales().forEach(s ->{
		 s.setGoodsReturn(goodsReturn);
		 });
		goodsReturnRepo.save(goodsReturn);
		redirectAttributes.addFlashAttribute("success", "Goods Returned updated successfully");
		return "redirect:/return-goods/edit";
	}
	public void pagination(int page, BillBookSearch billBookSearch, Model model) {

		page = (page > 0) ? page : 1;
		from = Constants.ROWS * (page - 1);
		records = (long) billBookRepository.count(billBookSearch);
		total = (int) Math.ceil((double) records / (double) Constants.ROWS);
		List<BillBookDTO> billBooks = billBookRepository.searchPaginated(billBookSearch, from);
		model.addAttribute("totalPages", total);
		model.addAttribute("currentPage", page);
		model.addAttribute("billBooks", billBooks);
		model.addAttribute("goodsReturnSearch", billBookSearch);
		System.out.println("total records: " + records + " total Pages: " + total + " Current page: " + page);
		fillModel(model);

	}
	
	
	public void editPagination(int page,GoodsReturnSearch goodsRetunSearch, Model model) {

		page = (page > 0) ? page : 1;
		from = Constants.ROWS * (page - 1);
		records = (long) goodsReturnRepo.count(goodsRetunSearch);
		total = (int) Math.ceil((double) records / (double) Constants.ROWS);
		List<GoodsReturn> goodsReturn = goodsReturnRepo.searchPaginated(goodsRetunSearch, from);
		model.addAttribute("totalPages", total);
		model.addAttribute("currentPage", page);
		model.addAttribute("goodsReturnList", goodsReturn);
		model.addAttribute("ReturnSearch", goodsRetunSearch);
		System.out.println("total records: " + records + " total Pages: " + total + " Current page: " + page);
		fillModel(model);

	}
	
	
	@GetMapping("/add/page={page}")
	public String page(@PathVariable("page") int page, HttpSession session, Model model) {
		BillBookSearch billBookSearch = (BillBookSearch) session.getAttribute("goodsReturnSearch");
		pagination(page, billBookSearch, model);
		return "goodsreturnsearch";
	}
	@GetMapping("/edit/page={page}")
	public String Editpage(@PathVariable("page") int page, HttpSession session, Model model) {
		GoodsReturnSearch goodsRetunSearch = (GoodsReturnSearch) session.getAttribute("ReturnSearch");
		editPagination(page, goodsRetunSearch, model);
		return "goodsreturneditsearch";
	}
	 
	
	private void fillModel(Model model) {
		String[] userTypes = { Constants.CUSTOMER, Constants.CONTRACTOR };
		model.addAttribute("customers", appUserRepository.findByUserType_NameInAndActive(userTypes, true));
		

	}
	
	

}
