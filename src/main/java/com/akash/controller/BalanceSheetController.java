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
import com.akash.repository.UserTypeRepository;
import com.akash.util.CommonMethods;

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
	@GetMapping
	public String add(Model model, HttpSession session) {
		
			
			model.addAttribute("userTypes", userTypeRepository.findAll());
			
		
		return "balancesheet";

	}
	@PostMapping
	public String getBalanceSheet(@RequestParam("userType") String[] usertype,Model model) {
		
		Double totalBalance=0.0;
		Double totalCredit=0.0;
		Double totalDebit=0.0;
		List<BalanceSheet> balanceSheets=new ArrayList<>();
		List<AppUser> users=appUserRepo.findAllAppUsersOnType(usertype);
		System.out.println("sizeeeee" + users.size());
		for(AppUser appUser:users) {
			BalanceSheet balanceSheet=new BalanceSheet(); 
			balanceSheet.setUser(appUser);
			System.out.println("appuserName" +appUser.getName());
			Double balance=CommonMethods.getBalance(appUser.getId(), LocalDate.MIN, LocalDate.now(),billBookRepository,dayBookRepo ,clearDuesRepo);
			balanceSheet.setBalance(balance);
			 totalBalance +=balance;
			Double credit=CommonMethods.getCredit(appUser.getId(), LocalDate.MIN, LocalDate.now(),billBookRepository,dayBookRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getDebit(appUser.getId(),LocalDate.MIN,LocalDate.now(), dayBookRepo, clearDuesRepo);
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		balanceSheets.add(balanceSheet);
		}
		System.out.println("balancesheetSizeee" + balanceSheets.size());
		
		model.addAttribute("totalcredit",totalCredit);
		model.addAttribute("totaldebit",totalDebit);
		model.addAttribute("totalbalance",totalBalance);
		model.addAttribute("list",balanceSheets);
		return "balancesheet";
	}

}
