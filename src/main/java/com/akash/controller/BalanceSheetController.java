package com.akash.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.akash.entity.AppUser;
import com.akash.entity.BalanceSheet;
import com.akash.projections.AppUserProjection;
import com.akash.repository.AppUserRepository;
import com.akash.repository.BillBookRepository;
import com.akash.repository.ClearDuesRepository;
import com.akash.repository.DayBookRepository;
import com.akash.repository.GoodsReturnRepository;
import com.akash.repository.ManufactureRepository;
import com.akash.repository.RawMaterialRepository;
import com.akash.repository.UserTypeRepository;
import com.akash.util.CommonMethods;
import com.akash.util.Constants;

@Controller
@RequestMapping("/balanceSheet")
public class BalanceSheetController {
	@Autowired
	UserTypeRepository userTypeRepository;
	@Autowired
	AppUserRepository appUserRepo;
	@Autowired
	BillBookRepository billBookRepository;

	@Autowired
	DayBookRepository dayBookRepo;
	@Autowired
	ClearDuesRepository clearDuesRepo;
	@Autowired
	GoodsReturnRepository goodsReturnRepository;
	
	@Autowired
	ManufactureRepository manufactureRepository;
	
	@Autowired
	RawMaterialRepository rawMaterialRepo;
	
	@GetMapping
	public String add(Model model, HttpSession session) {
		
			
			model.addAttribute("userTypes", userTypeRepository.findAll());
			
		
		return "balancesheet";

	}
	@PostMapping
	public String getBalanceSheet(@RequestParam("userType") String usertype,Model model,HttpSession session) {
	   session.setAttribute("userType", usertype);
	   model.addAttribute("userTypes", userTypeRepository.findAll());
			
		switch(usertype) {
		case Constants.CUSTOMER:
			getCustomerBalanceSheet( model);
			break;
		case Constants.CONTRACTOR:
			getCustomerBalanceSheet(model);
			break;
		case Constants.DRIVER:
			getDriverBalanceSheet(usertype, model);
			break;
		case Constants.DEALER:
			getDealerBalanceSheet(usertype, model);
			break;
		case Constants.LABOUR:
			getLabourBalanceSheet(usertype, model);
			break;
		case Constants.OWNER:
			getOwnerBalanceSheet(usertype, model);
			break;
		default:
			break;	
		
		}
		
		return "balancesheet";
		
	}
	public void getCustomerBalanceSheet(Model model) {
		String[] usertypes= { Constants.CUSTOMER, Constants.CONTRACTOR };
		List<AppUser> users=appUserRepo.findAllAppUsersOnType(usertypes);
		Double totalBalance=0.0;
		Double totalCredit=0.0;
		Double totalDebit=0.0;
		List<BalanceSheet> balanceSheets=new ArrayList<>();
		for(AppUser appUser:users) {
			BalanceSheet balanceSheet=new BalanceSheet(); 
			balanceSheet.setUser(appUser);
			System.out.println("appuserName" +appUser.getName());
			Double credit=CommonMethods.getCustomerCredit(appUser.getId(), LocalDate.MIN, LocalDate.now(),billBookRepository,dayBookRepo,goodsReturnRepository,clearDuesRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getCustomerDebit(appUser.getId(),LocalDate.MIN,LocalDate.now(), dayBookRepo );
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		     Double balance=credit-debit;
		     balanceSheet.setBalance(balance);
		     totalBalance +=balance;
		balanceSheets.add(balanceSheet);
		}
		System.out.println("balancesheetSizeee" + balanceSheets.size());
		
		model.addAttribute("totalcredit",totalCredit);
		model.addAttribute("totaldebit",totalDebit);
		model.addAttribute("totalbalance",totalBalance);
		model.addAttribute("list",balanceSheets);
		
	}
	
	public void getDriverBalanceSheet(String usertype,Model model) {
		String[] usertypes= { usertype };
		List<AppUser> users=appUserRepo.findAllAppUsersOnType(usertypes);
		Double totalBalance=0.0;
		Double totalCredit=0.0;
		Double totalDebit=0.0;
		List<BalanceSheet> balanceSheets=new ArrayList<>();
		for(AppUser appUser:users) {
			BalanceSheet balanceSheet=new BalanceSheet(); 
			balanceSheet.setUser(appUser);
			System.out.println("appuserName" +appUser.getName());
			Double credit=CommonMethods.getDriverCredit(appUser.getId(), LocalDate.MIN, LocalDate.now(),dayBookRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getDriverDebit(appUser.getId(),LocalDate.MIN,LocalDate.now(), dayBookRepo,billBookRepository,appUserRepo );
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		     Double balance=credit-debit;
		     balanceSheet.setBalance(balance);
		     totalBalance +=balance;
		balanceSheets.add(balanceSheet);
		}
		System.out.println("balancesheetSizeee" + balanceSheets.size());
		
		model.addAttribute("totalcredit",totalCredit);
		model.addAttribute("totaldebit",totalDebit);
		model.addAttribute("totalbalance",totalBalance);
		model.addAttribute("list",balanceSheets);
		
	}
	public void getOwnerBalanceSheet(String usertype,Model model) {
		String[] usertypes= { usertype };
		List<AppUser> users=appUserRepo.findAllAppUsersOnType(usertypes);
		Double totalBalance=0.0;
		Double totalCredit=0.0;
		Double totalDebit=0.0;
		List<BalanceSheet> balanceSheets=new ArrayList<>();
		for(AppUser appUser:users) {
			BalanceSheet balanceSheet=new BalanceSheet(); 
			balanceSheet.setUser(appUser);
			System.out.println("appuserName" +appUser.getName());
			Double credit=CommonMethods.getOwnerCredit(appUser.getId(), LocalDate.MIN, LocalDate.now(),dayBookRepo,appUserRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getOwnerDebit(appUser.getId(),LocalDate.MIN,LocalDate.now(), dayBookRepo,appUserRepo );
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		     Double balance=credit-debit;
		     balanceSheet.setBalance(balance);
		     totalBalance +=balance;
		balanceSheets.add(balanceSheet);
		}
		System.out.println("balancesheetSizeee" + balanceSheets.size());
		
		model.addAttribute("totalcredit",totalCredit);
		model.addAttribute("totaldebit",totalDebit);
		model.addAttribute("totalbalance",totalBalance);
		model.addAttribute("list",balanceSheets);
		
	}
	
	public void getLabourBalanceSheet(String usertype,Model model) {
		String[] usertypes= { usertype };
		List<AppUser> users=appUserRepo.findAllAppUsersOnType(usertypes);
		Double totalBalance=0.0;
		Double totalCredit=0.0;
		Double totalDebit=0.0;
		List<BalanceSheet> balanceSheets=new ArrayList<>();
		for(AppUser appUser:users) {
			BalanceSheet balanceSheet=new BalanceSheet(); 
			balanceSheet.setUser(appUser);
			System.out.println("appuserName" +appUser.getName());
			Double credit=CommonMethods.getLabourCredit(appUser.getId(), LocalDate.MIN, LocalDate.now(), dayBookRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getLabourDebit(appUser.getId(),LocalDate.MIN, LocalDate.now(), dayBookRepo, appUserRepo, billBookRepository, manufactureRepository);
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		     Double balance=credit-debit;
		     balanceSheet.setBalance(balance);
		     totalBalance +=balance;
		balanceSheets.add(balanceSheet);
		}
		System.out.println("balancesheetSizeee" + balanceSheets.size());
		
		model.addAttribute("totalcredit",totalCredit);
		model.addAttribute("totaldebit",totalDebit);
		model.addAttribute("totalbalance",totalBalance);
		model.addAttribute("list",balanceSheets);
		
	}
	
	public void getDealerBalanceSheet(String usertype,Model model) {
		String[] usertypes= { usertype };
		List<AppUser> users=appUserRepo.findAllAppUsersOnType(usertypes);
		Double totalBalance=0.0;
		Double totalCredit=0.0;
		Double totalDebit=0.0;
		List<BalanceSheet> balanceSheets=new ArrayList<>();
		for(AppUser appUser:users) {
			BalanceSheet balanceSheet=new BalanceSheet(); 
			balanceSheet.setUser(appUser);
			System.out.println("appuserName" +appUser.getName());
			Double credit=CommonMethods.getDealerCredit(appUser.getId(), LocalDate.MIN,LocalDate.now(), dayBookRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getDealerDebit(appUser.getId(), LocalDate.MIN, LocalDate.now(), dayBookRepo, rawMaterialRepo);
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		     Double balance=credit-debit;
		     balanceSheet.setBalance(balance);
		     totalBalance +=balance;
		balanceSheets.add(balanceSheet);
		}
		System.out.println("balancesheetSizeee" + balanceSheets.size());
		
		model.addAttribute("totalcredit",totalCredit);
		model.addAttribute("totaldebit",totalDebit);
		model.addAttribute("totalbalance",totalBalance);
		model.addAttribute("list",balanceSheets);
		
	}

}
