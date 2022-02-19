package com.akash.util;

import java.text.DecimalFormat;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akash.repository.BillBookRepository;
import com.akash.repository.DayBookRepository;

@Component
public class CommonMethods {
	static DecimalFormat df = new DecimalFormat("#.##");
	
	public static Double getBalance(Long id,LocalDate startDate,LocalDate endDate,BillBookRepository billBookRepo,DayBookRepository dayBookRepo) {
		Double prevBillBookCredit = billBookRepo.sumOfCustomerDebits(id,startDate,endDate);
		Double prevBillBookDiscount=billBookRepo.sumOfCustomerDiscounts(id,startDate,endDate);
		Double prevDayBookCredit = dayBookRepo.findUserCredits(id,startDate,endDate);
		Double prevDayBookDebit = dayBookRepo.findUserDebits(id,startDate,endDate);
		
		prevBillBookCredit = prevBillBookCredit==null?Double.valueOf(0):prevBillBookCredit;
		prevDayBookCredit = prevDayBookCredit==null?Double.valueOf(0):prevDayBookCredit;
		prevDayBookDebit = prevDayBookDebit==null?Double.valueOf(0):prevDayBookDebit;
		prevBillBookDiscount=prevBillBookDiscount==null?Double.valueOf(0):prevBillBookDiscount;
		
		Double prevBalance = (prevBillBookCredit+prevDayBookCredit)-(prevDayBookDebit+prevBillBookDiscount);
		 prevBalance=Double.valueOf(df.format(prevBalance));
		return prevBalance;
		
	}

}
