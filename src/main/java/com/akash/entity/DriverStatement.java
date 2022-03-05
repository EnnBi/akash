package com.akash.entity;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Transient;

import com.akash.util.CommonMethods;

public class DriverStatement{


	String receiptNumber;

	String customerName;

	String site;

	Double carraige;

	Double loading;

	Double unloading;

	Double total;
	
	Double debit;

	Double credit;

	Double balance;

	String reference;
	
	LocalDate date; 

	String type;
		
	@Transient
	DecimalFormat df = new DecimalFormat("#.##");
	public DriverStatement(Double debit, String reference,LocalDate date,String transactionNumber,String type) {
		super();
		this.debit = debit;
		this.reference = reference+"-"+transactionNumber;
		this.date = date;
		this.type = type;
	}
	
	

	public DriverStatement(String reference,Double credit,LocalDate date,String transactionNumber,String type) {
		this.credit = credit;
		this.reference = reference+"-"+transactionNumber;
		this.date = date;
		this.type = type;
	}
	
	
	//For getting details from BillBook
	public DriverStatement(LocalDate date, String receiptNumber, String customerName, String site, Double carraige,
			Double loading, Double unloading,String type) {
		this.date = date;
		this.receiptNumber = receiptNumber;
		this.customerName = customerName;
		this.site = site;
		this.carraige = carraige;
		this.loading = loading;
		this.unloading = unloading;
		this.type = type;
		calculateDebit();
	}

	public String getDate() {
		return  date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Double getCarraige() {
		return carraige;
	}

	public void setCarraige(Double carraige) {
		this.carraige = carraige;
		calculateDebit();
	}

	public Double getLoading() {
		return loading;
	}

	public void setLoading(Double loading) {
		this.loading = loading;
		calculateDebit();
	}

	public Double getUnloading() {
		return unloading;
	}

	public void setUnloading(Double unloading) {
		this.unloading = unloading;
		calculateDebit();
	}

	public Double getDebit() {
		return CommonMethods.format(debit);
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public Double getCredit() {
		return CommonMethods.format(credit);
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public Double getBalance() {
		return CommonMethods.format(balance);
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	void calculateDebit() {
		this.loading = this.loading == null ? 0 : this.loading;
		this.unloading = this.unloading == null ? 0 : this.unloading;
		this.debit = this.carraige + this.loading + this.unloading;
	}



	@Override
	public String toString() {
		return "DriverStatement [date=" + date + "]";
	}
	
	

}
