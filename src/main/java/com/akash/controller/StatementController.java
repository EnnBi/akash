package com.akash.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.akash.entity.AppUser;
import com.akash.entity.BillBook;
import com.akash.entity.CustomerStatement;
import com.akash.entity.DealerStatement;
import com.akash.entity.DriverStatement;
import com.akash.entity.LabourStatement;
import com.akash.entity.OwnerStatement;
import com.akash.entity.StatementSearch;
import com.akash.repository.AppUserRepository;
import com.akash.repository.BillBookRepository;
import com.akash.repository.ClearDuesRepository;
import com.akash.repository.DayBookRepository;
import com.akash.repository.GoodsReturnRepository;
import com.akash.repository.ManufactureRepository;
import com.akash.repository.RawMaterialRepository;
import com.akash.repository.UserTypeRepository;
import com.akash.util.BillBookToCustomerStatement;
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

@SuppressWarnings("deprecation")
@Controller
@RequestMapping("/statement")
public class StatementController {

	@Autowired
	BillBookRepository billBookRepo;

	@Autowired
	DayBookRepository dayBookRepo;

	@Autowired
	ManufactureRepository manufactureRepo;

	@Autowired
	RawMaterialRepository rawMaterialRepo;

	@Autowired
	UserTypeRepository userTypeRepo;
	@Autowired
	AppUserRepository appUserRepo;
	@Autowired
	ClearDuesRepository clearDuesRepository;
	@Autowired
	GoodsReturnRepository goodsReturnRepository;
	
	Double prevBalance = 0.0;
	Double totalCredit=0.0;
	Double totalDebit=0.0;
	String prevDate= LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	DecimalFormat df = new DecimalFormat("#.##");
	
	@RequestMapping(method = RequestMethod.GET)
	public String statement(Model model) {
		fillModel(model, new StatementSearch());
		model.addAttribute("statementSearch", new StatementSearch());
		return "statement";
	}

	@RequestMapping(method = RequestMethod.POST,params="view")
	public String statementShow(StatementSearch statementSearch, Model model) {
		this.prevBalance=0.0;
		this.totalCredit=0.0;
		this.totalDebit=0.0;
		switch (statementSearch.getUserType()) {
		case Constants.DRIVER:
			model.addAttribute("statements", generateDriverStatement(statementSearch,model));
			model.addAttribute("driverStatement", true);
			break;
		case Constants.DEALER:
			model.addAttribute("statements", generateDealerStatement(statementSearch,model));
			model.addAttribute("dealerStatement", true);
			break;
		case Constants.LABOUR:
			model.addAttribute("statements", generateLabourStatement(statementSearch,model));
			model.addAttribute("labourStatement", true);
			break;
		case Constants.OWNER:
			model.addAttribute("statements", generateOwnerStatement(statementSearch,model));
			model.addAttribute("ownerStatement", true);
			break;
		case Constants.CUSTOMER:
			model.addAttribute("statements", generateCustomerStatement(statementSearch,model));
			model.addAttribute("customerStatement", true);
			break;
		case Constants.CONTRACTOR:
			model.addAttribute("statements", generateCustomerStatement(statementSearch,model));
			model.addAttribute("customerStatement", true);
			break;
		default:
			break;
		}
		fillModel(model, statementSearch);
		model.addAttribute("date",LocalDate.now());
		model.addAttribute("user", appUserRepo.findById(statementSearch.getUser()).orElse(null));
		model.addAttribute("statementSearch", statementSearch);
		model.addAttribute("previousBalance", CommonMethods.format(prevBalance));
		model.addAttribute("totalCredit", CommonMethods.format(totalCredit));
		model.addAttribute("totalDebit", CommonMethods.format(totalDebit));
		model.addAttribute("prevDate", statementSearch.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		return "statement";
	}
	
	@RequestMapping(method = RequestMethod.POST,params="excel")
	public void statementPrintExcel(StatementSearch statementSearch, Model model,HttpServletResponse response) {
		AppUser user = appUserRepo.findById(statementSearch.getUser()).orElse(null);
		this.prevBalance=0.0;
		this.totalCredit=0.0;
		this.totalDebit=0.0;
		this.prevDate=statementSearch.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		switch (statementSearch.getUserType()) {
		case Constants.DRIVER:
			List<DriverStatement> driverStatements = generateDriverStatement(statementSearch,model);
			generateDriverStatementExcel(driverStatements, response, user);
			break;
		case Constants.DEALER:
			List<DealerStatement> dealerStatements =  generateDealerStatement(statementSearch,model);
			generateDealerStatementExcel(dealerStatements, response, user);
			break;
		case Constants.LABOUR:
			List<LabourStatement> labourStatements =  generateLabourStatement(statementSearch,model);
			generateLabourStatementExcel(labourStatements, response, user);
			break;
		case Constants.OWNER:
			List<OwnerStatement> ownerStatements = generateOwnerStatement(statementSearch,model);
			generateOwnerStatementExcel(ownerStatements, response, user);
			break;
		case Constants.CUSTOMER:
			List<CustomerStatement> customerStatements = generateCustomerStatement(statementSearch, model);
			generateCustomerStatementExcel(customerStatements, response, user);
			break;
		case Constants.CONTRACTOR:
			List<CustomerStatement> contracterStatements = generateCustomerStatement(statementSearch, model);
			generateCustomerStatementExcel(contracterStatements, response, user);
			break;
		default:
			break;
		}


	}

	public void fillModel(Model model, StatementSearch statementSearch) {
		model.addAttribute("userTypes", userTypeRepo.findAll());
		model.addAttribute("users", appUserRepo.findByUserType_NameAndActive(statementSearch.getUserType(), true));
	}

	public List<DriverStatement> generateDriverStatement(StatementSearch statementSearch,Model model) {
		LocalDate previousDay = statementSearch.getStartDate().minusDays(1);
		AppUser driver = appUserRepo.findById(statementSearch.getUser()).orElse(null);
		Double previousBalance=CommonMethods.getDriverCredit(statementSearch.getUser(), LocalDate.MIN, previousDay, dayBookRepo)-
				CommonMethods.getDriverDebit(statementSearch.getUser(), LocalDate.MIN, previousDay, dayBookRepo, billBookRepo, appUserRepo);
		Double balance = previousBalance;

		List<DriverStatement> billBookEntries = billBookRepo.findDriverDebits(driver, statementSearch.getStartDate(),
				statementSearch.getEndDate());
		List<DriverStatement> dayBookDebitEntries = dayBookRepo.findDriverDebitsBetweenDates(statementSearch.getUser(),
				statementSearch.getStartDate(), statementSearch.getEndDate());
		List<DriverStatement> dayBookCreditEntries = dayBookRepo.findDriverCreditsBetweenDates(
				statementSearch.getUser(), statementSearch.getStartDate(), statementSearch.getEndDate());

		List<DriverStatement> statements = new ArrayList<DriverStatement>();
		statements.addAll(billBookEntries);
		statements.addAll(dayBookCreditEntries);
		statements.addAll(dayBookDebitEntries);

		statements.sort(Comparator.comparing(DriverStatement::getDate));
		for (DriverStatement s : statements) {
			if (Constants.BILLBOOK.equals(s.getType())) {
				s.setBalance(balance + s.getDebit());
				balance = balance + s.getDebit();
				this.totalDebit=this.totalDebit+s.getDebit();
			} else {
				if (s.getCredit() != null) {
					s.setBalance(balance - s.getCredit());
					balance = balance - s.getCredit();
					this.totalCredit=this.totalCredit+s.getCredit();
				} else {
					s.setBalance(balance + s.getDebit());
					balance = balance + s.getDebit();
					this.totalDebit=this.totalDebit+s.getDebit();
				}

			}
		}
		return statements;

	}

	public List<DealerStatement> generateDealerStatement(StatementSearch statementSearch,Model model) {
		LocalDate previousDay = statementSearch.getStartDate().minusDays(1);

		Double previousBalance = CommonMethods.getDealerCredit(statementSearch.getUser(),LocalDate.MIN, previousDay, dayBookRepo)-CommonMethods.getDealerDebit(statementSearch.getUser(),LocalDate.MIN, previousDay, dayBookRepo, rawMaterialRepo);
				
		Double balance=previousBalance;
		this.prevBalance=Double.valueOf(df.format(previousBalance));;
		
		List<DealerStatement> rawMaterialEntries = rawMaterialRepo.findDealerDebits(statementSearch.getUser(),statementSearch.getStartDate(),statementSearch.getEndDate());
		List<DealerStatement> dayDebitEntries = dayBookRepo.findDealerDebitsBetweenDates(statementSearch.getUser(),statementSearch.getStartDate(),statementSearch.getEndDate());
		List<DealerStatement> dayCreditEntries = dayBookRepo.findDealerCreditsBetweenDates(statementSearch.getUser(),statementSearch.getStartDate(),statementSearch.getEndDate());
		
		List<DealerStatement> statements = new ArrayList<>();
		statements.addAll(rawMaterialEntries);
		statements.addAll(dayCreditEntries);
		statements.addAll(dayDebitEntries);
		
		Collections.sort(statements, (a, b) -> a.getDate().compareTo(b.getDate()));
		
		for (DealerStatement s : statements) {
			if (Constants.RAW_MATERIAL.equals(s.getType())) {
				s.setBalance(balance + s.getDebit());
				balance = balance + s.getDebit();
				this.totalDebit=this.totalDebit+s.getDebit();
			} else {
				if (s.getCredit() != null) {
					s.setBalance(balance - s.getCredit());
					balance = balance - s.getCredit();
					this.totalCredit=this.totalCredit+s.getCredit();
				} else {
					s.setBalance(balance + s.getDebit());
					balance = balance + s.getDebit();
					this.totalDebit=this.totalDebit+s.getDebit();
				}

			}
		}
		return statements;
	}
	
	
	public List<LabourStatement> generateLabourStatement(StatementSearch search,Model model){
		LocalDate previousDay = search.getStartDate().minusDays(1);
		AppUser labour = appUserRepo.findById(search.getUser()).orElse(null);
		
		Double previousBalance=CommonMethods.getLabourCredit(search.getUser(), LocalDate.MIN, previousDay, dayBookRepo)-
				CommonMethods.getLabourDebit(search.getUser(), LocalDate.MIN, previousDay, dayBookRepo, appUserRepo, billBookRepo, manufactureRepo);
				
		Double balance = previousBalance;
		this.prevBalance=Double.valueOf(df.format(previousBalance));
		
		List<LabourStatement> billBookEntries = billBookRepo.findLabourBillBookDebits(labour, search.getStartDate(), search.getEndDate());
		List<LabourStatement> manufactureEntries = manufactureRepo.findLabourDebits(labour, search.getStartDate(), search.getEndDate());
		List<LabourStatement> dayDebitEntries = dayBookRepo.findLabourStatementDebitsBetweenDates(search.getUser(),search.getStartDate(),search.getEndDate());
		List<LabourStatement> dayCreditEntries = dayBookRepo.findLabourStatementCreditsBetweenDates(search.getUser(),search.getStartDate(),search.getEndDate());
		
		List<LabourStatement> statements = new ArrayList<>();
		
		statements.addAll(billBookEntries);
		statements.addAll(manufactureEntries);
		statements.addAll(dayDebitEntries);
		statements.addAll(dayCreditEntries);
		
		Collections.sort(statements, (a, b) -> a.getDate().compareTo(b.getDate()));
		
		for (LabourStatement s : statements) {
			if (Constants.MANUFACTURE.equals(s.getType())) {
				s.setBalance(balance + s.getDebit());
				balance = balance + s.getDebit();
				this.totalDebit=this.totalDebit+s.getDebit();
			}
			else if(Constants.BILLBOOK.equals(s.getType())){
				s.setBalance(balance+s.getDebit());
				balance=balance+s.getDebit();
				this.totalDebit=this.totalDebit+s.getDebit();
			}else {
				if (s.getCredit() != null) {
					s.setBalance(balance - s.getCredit());
					balance = balance - s.getCredit();
					this.totalCredit=this.totalCredit+s.getCredit();
				} else {
					s.setBalance(balance + s.getDebit());
					balance = balance + s.getDebit();
					this.totalDebit=this.totalDebit+s.getDebit();
				}

			}
		}
		return statements;
	}
	
	
	public List<OwnerStatement> generateOwnerStatement(StatementSearch search,Model model){
		LocalDate previousDay = search.getStartDate().minusDays(1);
		AppUser owner = appUserRepo.findById(search.getUser()).orElse(null);
		Double prevBalance=CommonMethods.getOwnerCredit(search.getUser(), LocalDate.MIN, previousDay, dayBookRepo, appUserRepo)-
				CommonMethods.getOwnerDebit(search.getUser(), LocalDate.MIN, previousDay, dayBookRepo, appUserRepo);
		Double balance = prevBalance;
		
		List<OwnerStatement> statements = dayBookRepo.findByAccountNumberAndDateBetween(owner.getAccountNumber(),search.getStartDate(),search.getEndDate());
		
		Collections.sort(statements, (a, b) -> a.getDate().compareTo(b.getDate()));
		
		for (OwnerStatement s : statements) {
			if(Constants.EXPENDITURE.equals(s.getTransactionType())){
				s.setBalance(balance-s.getAmount());
				s.setDebit(s.getAmount());
				balance=balance-s.getAmount();
			}else{
				s.setBalance(balance+s.getAmount());
				s.setCredit(s.getAmount());
				balance=balance+s.getAmount();
			}
		} 
		
		return statements;
	}
	
	public List<CustomerStatement> generateCustomerStatement(StatementSearch search,Model model){
		LocalDate previousDay = search.getStartDate().minusDays(1);
		
		Double balance = CommonMethods.getCustomerCredit(search.getUser(), LocalDate.MIN, previousDay, billBookRepo, dayBookRepo, goodsReturnRepository, clearDuesRepository)-
				CommonMethods.getCustomerDebit(search.getUser(), previousDay, previousDay, dayBookRepo);
		this.prevBalance=balance;
		
		
		List<BillBook> customerBillBook = billBookRepo.findByCustomer_IdAndDateBetween(search.getUser(),search.getStartDate(),search.getEndDate());
		List<CustomerStatement> billBookCreditEntries = BillBookToCustomerStatement.convert(customerBillBook);
		billBookCreditEntries.forEach(b->{
			b.setSales(billBookRepo.findSalesOnBillBookId(b.getId()));
		});
		
		List<CustomerStatement> dayBookCreditEntries = dayBookRepo.findCustomerCreditsBetweenDates(search.getUser(),search.getStartDate(),search.getEndDate());
		List<CustomerStatement> dayBookDebitEntries = dayBookRepo.findCustomerDebitsBetweenDates(search.getUser(),search.getStartDate(),search.getEndDate());
		List<CustomerStatement> clearDuesEntries=clearDuesRepository.findCustomerClearDuesBetweenDates(search.getUser(),search.getStartDate(),search.getEndDate());
		List<CustomerStatement> statements = new ArrayList<>();
		statements.addAll(billBookCreditEntries);
		statements.addAll(dayBookCreditEntries);
		statements.addAll(dayBookDebitEntries);
		statements.addAll(clearDuesEntries);
		
		Collections.sort(statements, (a, b) -> a.getDate().compareTo(b.getDate()));
		
		for (CustomerStatement s : statements) {
			if(Constants.BILLBOOK.equals(s.getType())){
				s.setBalance(balance+s.getCredit());
				balance=balance+s.getCredit();
			}else if(Constants.DUES.equals(s.getType())) {
				s.setBalance(balance-s.getDebit());
				balance=balance-s.getDebit();
				this.totalDebit=this.totalDebit+s.getDebit();
			}
			else if(Constants.RG.equals(s.getType())){
				s.setBalance(balance-s.getDebit());
				balance=balance-s.getDebit();
				this.totalDebit=this.totalDebit+s.getDebit();
				
			}
			else{
				if(s.getDebit() != null){
					s.setBalance(balance-s.getDebit());
					balance=balance-s.getDebit();
					this.totalDebit=this.totalDebit+s.getDebit();
					
				}
				else{
					s.setBalance(balance+s.getCredit());
					balance=balance+s.getCredit();
					this.totalCredit=this.totalCredit+s.getCredit();
				}
			}
		}
		return statements;
		
	}
	
	public void generateCustomerStatementExcel(List<CustomerStatement> statements, HttpServletResponse response,AppUser user){
		InputStream mainJasperStream = this.getClass().getResourceAsStream("/Customer.jasper");
		InputStream subJasperStream = this.getClass().getResourceAsStream("/Sales.jasper");
		
		try {
			JasperReport mainReport =  (JasperReport) JRLoader.loadObject(mainJasperStream);
			JasperReport salesReport =  (JasperReport) JRLoader.loadObject(subJasperStream);
			
			Map<String,Object> params =  new HashMap<>();
			params.put("salesReport", salesReport);
			params.put("name", user.getLabelName());
			params.put("contact",user.getContact());
			params.put("date",LocalDate.now());
			params.put("prevDate",this.prevDate);
			params.put("prevBalance",this.prevBalance);
			JRDataSource data = new JRBeanCollectionDataSource(statements);
			JasperPrint jasperPrint  = JasperFillManager.fillReport(mainReport, params, data);
			response.setContentType("application/vnd.ms-excel");
			String fileName = user.getLabelName()+" "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
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
	
	public void generateDealerStatementExcel(List<DealerStatement> statements, HttpServletResponse response,AppUser user){
		InputStream mainJasperStream = this.getClass().getResourceAsStream("/Dealer.jasper");
		
		try {
			JasperReport mainReport =  (JasperReport) JRLoader.loadObject(mainJasperStream);
			
			Map<String,Object> params =  new HashMap<>();
			params.put("name", user.getLabelName());
			params.put("contact",user.getContact());
			params.put("date",LocalDate.now());
			params.put("prevBalance",this.prevBalance);
			params.put("prevDate",this.prevDate);
			JRDataSource data = new JRBeanCollectionDataSource(statements);
			JasperPrint jasperPrint  = JasperFillManager.fillReport(mainReport, params, data);
			response.setContentType("application/vnd.ms-excel");
			String fileName = user.getLabelName()+" "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
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
	
	public void generateDriverStatementExcel(List<DriverStatement> statements, HttpServletResponse response,AppUser user){
		InputStream mainJasperStream = this.getClass().getResourceAsStream("/Driver.jasper");
		
		try {
			JasperReport mainReport =  (JasperReport) JRLoader.loadObject(mainJasperStream);
			
			Map<String,Object> params =  new HashMap<>();
			params.put("name", user.getLabelName());
			params.put("contact",user.getContact());
			params.put("date",LocalDate.now());
			params.put("prevBalance",this.prevBalance);
			params.put("prevDate",this.prevDate);
			JRDataSource data = new JRBeanCollectionDataSource(statements);
			JasperPrint jasperPrint  = JasperFillManager.fillReport(mainReport, params, data);
			response.setContentType("application/vnd.ms-excel");
			String fileName = user.getLabelName()+" "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
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
	
	public void generateOwnerStatementExcel(List<OwnerStatement> statements, HttpServletResponse response,AppUser user){
		InputStream mainJasperStream = this.getClass().getResourceAsStream("/Owner.jasper");
		
		try {
			JasperReport mainReport =  (JasperReport) JRLoader.loadObject(mainJasperStream);
			
			Map<String,Object> params =  new HashMap<>();
			params.put("name", user.getLabelName());
			params.put("contact",user.getContact());
			params.put("date",LocalDate.now());
			params.put("prevBalance",this.prevBalance);
			params.put("prevDate",this.prevDate);
			JRDataSource data = new JRBeanCollectionDataSource(statements);
			JasperPrint jasperPrint  = JasperFillManager.fillReport(mainReport, params, data);
			response.setContentType("application/vnd.ms-excel");
			String fileName = user.getLabelName()+" "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
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
	
	public void generateLabourStatementExcel(List<LabourStatement> statements, HttpServletResponse response,AppUser user){
		InputStream mainJasperStream = this.getClass().getResourceAsStream("/Labour.jasper");
		
		try {
			JasperReport mainReport =  (JasperReport) JRLoader.loadObject(mainJasperStream);
			
			Map<String,Object> params =  new HashMap<>();
			params.put("name", user.getLabelName());
			params.put("contact",user.getContact());
			params.put("date",LocalDate.now());
			params.put("prevBalance",this.prevBalance);
			params.put("prevDate",this.prevDate);
			JRDataSource data = new JRBeanCollectionDataSource(statements);
			JasperPrint jasperPrint  = JasperFillManager.fillReport(mainReport, params, data);
			response.setContentType("application/vnd.ms-excel");
			String fileName = user.getLabelName()+" "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
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
