package com.akash.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.akash.entity.AppUser;
import com.akash.entity.BalanceSheet;
import com.akash.entity.StatementSearch;
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

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

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
		model.addAttribute("balanceSheet",new StatementSearch());

		return "balancesheet";

	}
	@PostMapping(params="view")
	public String getBalanceSheet(@ModelAttribute("balanceSheet") StatementSearch search,Model model,HttpSession session) {
	
	   model.addAttribute("userTypes", userTypeRepository.findAll());
	   Map<String,Object> map = new HashMap<>();	
	   
		switch(search.getUserType()) {
		case Constants.CUSTOMER:
			map = getCustomerBalanceSheet(search.getStartDate(),search.getEndDate());
			break;
		case Constants.CONTRACTOR:
			map = getCustomerBalanceSheet(search.getStartDate(),search.getEndDate());
			break;
		case Constants.DRIVER:
			map= getDriverBalanceSheet(search.getStartDate(),search.getEndDate());
			break;
		case Constants.DEALER:
			map = getDealerBalanceSheet(search.getStartDate(),search.getEndDate());
			break;
		case Constants.LABOUR:
			map = getLabourBalanceSheet(search.getStartDate(),search.getEndDate());
			break;
		case Constants.OWNER:
			map =getOwnerBalanceSheet(search.getStartDate(),search.getEndDate());
			break;
		default:
			break;	
		
		}
		
		model.addAttribute("totalCredit",CommonMethods.format(map.get("totalCredit")));
		model.addAttribute("totalDebit",CommonMethods.format(map.get("totalDebit")));
		model.addAttribute("totalBalance",CommonMethods.format(map.get("totalBalance")));
		model.addAttribute("balanceSheets",map.get("balanceSheets"));
				
		
		return "balancesheet";
		
	}
	
	@PostMapping(params = "excel")
	public void exportToExcelBalanceSheet(@ModelAttribute("balanceSheet") StatementSearch search, Model model,HttpServletResponse response,HttpSession session) {
		
		   model.addAttribute("userTypes", userTypeRepository.findAll());
		   Map<String,Object> map = new HashMap<>();		
			switch(search.getUserType()) {
			case Constants.CUSTOMER:
				 map = getCustomerBalanceSheet(search.getStartDate(),search.getEndDate());
				break;
			case Constants.CONTRACTOR:
				 map = getCustomerBalanceSheet(search.getStartDate(),search.getEndDate());
				break;
			case Constants.DRIVER:
				 map = getDriverBalanceSheet(search.getStartDate(),search.getEndDate());
				break;
			case Constants.DEALER:
				 map = getDealerBalanceSheet(search.getStartDate(),search.getEndDate());
				break;
			case Constants.LABOUR:
				 map = getLabourBalanceSheet(search.getStartDate(),search.getEndDate());
				break;
			case Constants.OWNER:
				 map = getOwnerBalanceSheet(search.getStartDate(),search.getEndDate());
				break;
			default:
				break;	
			
			}
		generateStatementExcel(map,response,search.getUserType());
	}

	public Map<String, Object> getCustomerBalanceSheet(LocalDate startDate,LocalDate endDate) {
		Map<String, Object> map = new HashMap<>();
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
			Double credit=CommonMethods.getCustomerCredit(appUser.getId(),startDate,endDate,billBookRepository,dayBookRepo,goodsReturnRepository,clearDuesRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getCustomerDebit(appUser.getId(),startDate,endDate, dayBookRepo );
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		     Double balance=credit-debit;
		     balanceSheet.setBalance(balance);
		     totalBalance +=balance;
		balanceSheets.add(balanceSheet);
		}
		System.out.println("balancesheetSizeee" + balanceSheets.size());
		List<BalanceSheet> newBalanceSheets=balanceSheets.stream().filter(b->b.getBalance()>Double.valueOf(50)).collect(Collectors.toList());
		map.put("totalCredit",totalCredit);
		map.put("totalDebit",totalDebit);
		map.put("totalBalance",totalBalance);
		map.put("balanceSheets",newBalanceSheets);
		
		return map;
		
	}
	
	public Map<String, Object> getDriverBalanceSheet(LocalDate startDate,LocalDate endDate) {
		Map<String, Object> map = new HashMap<>();
		String[] usertypes= { Constants.DRIVER };
		
		List<AppUser> users=appUserRepo.findAllAppUsersOnType(usertypes);
		Double totalBalance=0.0;
		Double totalCredit=0.0;
		Double totalDebit=0.0;
		List<BalanceSheet> balanceSheets=new ArrayList<>();
		for(AppUser appUser:users) {
			BalanceSheet balanceSheet=new BalanceSheet(); 
			balanceSheet.setUser(appUser);
			System.out.println("appuserName" +appUser.getName());
			Double credit=CommonMethods.getDriverCredit(appUser.getId(),startDate,endDate,dayBookRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getDriverDebit(appUser.getId(),startDate,endDate, dayBookRepo,billBookRepository,appUserRepo );
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		     Double balance=debit-credit;
		     balanceSheet.setBalance(balance);
		     totalBalance +=balance;
		balanceSheets.add(balanceSheet);
		}
		System.out.println("balancesheetSizeee" + balanceSheets.size());
		List<BalanceSheet> newBalanceSheets=balanceSheets.stream().filter(b->b.getBalance()>Double.valueOf(50)).collect(Collectors.toList());
		
		map.put("totalCredit",totalCredit);
		map.put("totalDebit",totalDebit);
		map.put("totalBalance",totalBalance);
		map.put("balanceSheets",newBalanceSheets);
		
		return map;
		
	}
	public Map<String, Object> getOwnerBalanceSheet(LocalDate startDate,LocalDate endDate) {
		Map<String, Object> map = new HashMap<>();
		String[] usertypes= { Constants.OWNER };
		List<AppUser> users=appUserRepo.findAllAppUsersOnType(usertypes);
		Double totalBalance=0.0;
		Double totalCredit=0.0;
		Double totalDebit=0.0;
		List<BalanceSheet> balanceSheets=new ArrayList<>();
		for(AppUser appUser:users) {
			BalanceSheet balanceSheet=new BalanceSheet(); 
			balanceSheet.setUser(appUser);
			System.out.println("appuserName" +appUser.getName());
			Double credit=CommonMethods.getOwnerCredit(appUser.getId(),startDate,endDate,dayBookRepo,appUserRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getOwnerDebit(appUser.getId(),startDate,endDate, dayBookRepo,appUserRepo );
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		     Double balance=debit-credit;
		     balanceSheet.setBalance(balance);
		     totalBalance +=balance;
		balanceSheets.add(balanceSheet);
		}
		System.out.println("balancesheetSizeee" + balanceSheets.size());
		List<BalanceSheet> newBalanceSheets=balanceSheets.stream().filter(b->b.getBalance()>Double.valueOf(50)).collect(Collectors.toList());
		map.put("totalCredit",CommonMethods.format(totalCredit));
		map.put("totalDebit",CommonMethods.format(totalDebit));
		map.put("totalBalance",CommonMethods.format(totalBalance) );
		map.put("balanceSheets",newBalanceSheets);
		
		return map;
		
	}
	
	public Map<String, Object> getLabourBalanceSheet(LocalDate startDate,LocalDate endDate) {
		Map<String, Object> map = new HashMap<>();
		String[] usertypes= { Constants.LABOUR };
		List<AppUser> users=appUserRepo.findAllAppUsersOnType(usertypes);
		Double totalBalance=0.0;
		Double totalCredit=0.0;
		Double totalDebit=0.0;
		List<BalanceSheet> balanceSheets=new ArrayList<>();
		for(AppUser appUser:users) {
			BalanceSheet balanceSheet=new BalanceSheet(); 
			balanceSheet.setUser(appUser);
			System.out.println("appuserName" +appUser.getName());
			Double credit=CommonMethods.getLabourCredit(appUser.getId(), startDate, endDate, dayBookRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getLabourDebit(appUser.getId(),startDate, endDate, dayBookRepo, appUserRepo, billBookRepository, manufactureRepository);
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		     Double balance=debit-credit;
		     balanceSheet.setBalance(balance);
		     totalBalance +=balance;
		balanceSheets.add(balanceSheet);
		}
		List<BalanceSheet> newBalanceSheets=balanceSheets.stream().filter(b->b.getBalance()>Double.valueOf(50)).collect(Collectors.toList());
		map.put("totalCredit",totalCredit);
		map.put("totalDebit",totalDebit);
		map.put("totalBalance",totalBalance);
		map.put("balanceSheets",newBalanceSheets);
		
		return map;
		
	}
	
	public Map<String, Object> getDealerBalanceSheet(LocalDate startDate,LocalDate endDate) {
		Map<String, Object> map = new HashMap<>();
		String[] usertypes= { Constants.DEALER };
		List<AppUser> users=appUserRepo.findAllAppUsersOnType(usertypes);
		Double totalBalance=0.0;
		Double totalCredit=0.0;
		Double totalDebit=0.0;
		List<BalanceSheet> balanceSheets=new ArrayList<>();
		for(AppUser appUser:users) {
			BalanceSheet balanceSheet=new BalanceSheet(); 
			balanceSheet.setUser(appUser);
			System.out.println("appuserName" +appUser.getName());
			Double credit=CommonMethods.getDealerCredit(appUser.getId(), startDate,endDate, dayBookRepo);
			balanceSheet.setCredit(credit);
			totalCredit+=credit;
			Double debit=CommonMethods.getDealerDebit(appUser.getId(),startDate,endDate, dayBookRepo, rawMaterialRepo);
		     balanceSheet.setDebit(debit);
		     totalDebit+=debit;
		     Double balance=debit-credit;
		     balanceSheet.setBalance(balance);
		     totalBalance +=balance;
		balanceSheets.add(balanceSheet);
		} 
		System.out.println("balancesheetSizeee" + balanceSheets.size());
		List<BalanceSheet> newBalanceSheets=balanceSheets.stream().filter(b->b.getBalance()>Double.valueOf(50)).collect(Collectors.toList());
		map.put("totalCredit",totalCredit);
		map.put("totalDebit",totalDebit);
		map.put("totalBalance",totalBalance);
		map.put("balanceSheets",newBalanceSheets);
		
		return map;
	}

	public void generateStatementExcel(Map<String,Object> map,HttpServletResponse response,String usertype){
		InputStream mainJasperStream = this.getClass().getResourceAsStream("/BalanceSheet.jasper");
		
		try {
			JasperReport mainReport =  (JasperReport) JRLoader.loadObject(mainJasperStream);
			
			Map<String,Object> params =  new HashMap<>();
			params.put("totalBalance", map.get("totalBalance"));
			params.put("totalCredit",map.get("totalCredit"));
			params.put("totalDebit",map.get("totalDebit"));
			params.put("type",usertype);
			JRDataSource data = new JRBeanCollectionDataSource((Collection<BalanceSheet>) map.get("balanceSheets"));
			JasperPrint jasperPrint  = JasperFillManager.fillReport(mainReport, params, data);
			response.setContentType("application/vnd.ms-excel");
			String fileName = usertype+" "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
			OutputStream  output = response.getOutputStream();

			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);

			exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);

			exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.TRUE);
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.exportReport();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
