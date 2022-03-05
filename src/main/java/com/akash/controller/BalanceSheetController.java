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

import javax.servlet.http.HttpServletResponse;
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

		return "balancesheet";

	}
	@PostMapping(params="view")
	public String getBalanceSheet(@RequestParam("userType") String usertype,Model model,HttpSession session) {
	   session.setAttribute("userType", usertype);
	   model.addAttribute("userTypes", userTypeRepository.findAll());
	   Map<String,Object> map = new HashMap<>();	
		switch(usertype) {
		case Constants.CUSTOMER:
			map = getCustomerBalanceSheet();
			break;
		case Constants.CONTRACTOR:
			map = getCustomerBalanceSheet();
			break;
		case Constants.DRIVER:
			map= getDriverBalanceSheet();
			break;
		case Constants.DEALER:
			map = getDealerBalanceSheet();
			break;
		case Constants.LABOUR:
			map = getLabourBalanceSheet();
			break;
		case Constants.OWNER:
			map =getOwnerBalanceSheet();
			break;
		default:
			break;	
		
		}
		
		model.addAttribute("totalCredit",map.get("totalCredit"));
		model.addAttribute("totalDebit",map.get("totalDebit"));
		model.addAttribute("totalBalance",map.get("totalBalance"));
		model.addAttribute("balanceSheets",map.get("balanceSheets"));
		
		return "balancesheet";
		
	}
	
	@PostMapping(params = "excel")
	public void exportToExcelBalanceSheet(@RequestParam("userType") String usertype, Model model,HttpServletResponse response,HttpSession session) {
		session.setAttribute("userType", usertype);
		   model.addAttribute("userTypes", userTypeRepository.findAll());
		   Map<String,Object> map = new HashMap<>();		
			switch(usertype) {
			case Constants.CUSTOMER:
				 map = getCustomerBalanceSheet();
				break;
			case Constants.CONTRACTOR:
				 map = getCustomerBalanceSheet();
				break;
			case Constants.DRIVER:
				 map = getDriverBalanceSheet();
				break;
			case Constants.DEALER:
				 map = getDealerBalanceSheet();
				break;
			case Constants.LABOUR:
				 map = getLabourBalanceSheet();
				break;
			case Constants.OWNER:
				 map = getOwnerBalanceSheet();
				break;
			default:
				break;	
			
			}
		generateStatementExcel(map,response,usertype);
	}

	public Map<String, Object> getCustomerBalanceSheet() {
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
		
		map.put("totalCredit",totalCredit);
		map.put("totalDebit",totalDebit);
		map.put("totalBalance",totalBalance);
		map.put("balanceSheets",balanceSheets);
		
		return map;
		
	}
	
	public Map<String, Object> getDriverBalanceSheet() {
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
		
		map.put("totalCredit",totalCredit);
		map.put("totalDebit",totalDebit);
		map.put("totalBalance",totalBalance);
		map.put("balanceSheets",balanceSheets);
		
		return map;
		
	}
	public Map<String, Object> getOwnerBalanceSheet() {
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
		
		map.put("totalCredit",totalCredit);
		map.put("totalDebit",totalDebit);
		map.put("totalBalance",totalBalance);
		map.put("balanceSheets",balanceSheets);
		
		return map;
		
	}
	
	public Map<String, Object> getLabourBalanceSheet() {
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
		
		map.put("totalCredit",totalCredit);
		map.put("totalDebit",totalDebit);
		map.put("totalBalance",totalBalance);
		map.put("balanceSheets",balanceSheets);
		
		return map;
		
	}
	
	public Map<String, Object> getDealerBalanceSheet() {
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
		
		map.put("totalCredit",totalCredit);
		map.put("totalDebit",totalDebit);
		map.put("totalBalance",totalBalance);
		map.put("balanceSheets",balanceSheets);
		
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
