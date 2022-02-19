package com.akash.entity;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.akash.util.Constants;

public class CustomerStatement {

	long id;
	
	String receiptNumber;
	
	LocalDate date;
	
	String vehicleNo;
	
	String address;
	
	Double loading;
	
	Double unloading;
	
	Double carraige;
	
	Double debit;
	
	Double amount;
		
	Double credit;
	
	Double balance;
	
	String reference;
	
	List<SalesStatement> sales;

	String type;
	Double discount;
	
	DecimalFormat df = new DecimalFormat("#.##");
	
	public CustomerStatement(long id, String receiptNumber, LocalDate date,Vehicle vehicle, String vehicleNo, String address,String site,
			Double loading, Double unloading, Double carraige,Double discount,Double credit)
	{
		super();
		this.id = id;
		this.receiptNumber = receiptNumber;
		this.date = date;
		if(vehicle != null)
			this.vehicleNo = vehicle.getNumber();
		else
			this.vehicleNo=vehicleNo;
		this.loading = loading;
		this.unloading = unloading;
		this.carraige = carraige;
		this.type = Constants.BILLBOOK;
		this.discount=discount;
		this.credit=credit;
		if(site != null && !("".equals(site))){
				this.address = site;
		}else
			this.address=address;
		calculatetotal();
	}
	
	

	private void calculatetotal() {
		this.loading = this.loading==null?Double.valueOf(0):this.loading;
		this.unloading = this.unloading==null?Double.valueOf(0):this.unloading;
		this.carraige = this.carraige==null?Double.valueOf(0):this.carraige;
		this.credit = this.credit==null?Double.valueOf(0):this.credit;
		this.discount = this.discount==null?Double.valueOf(0):this.discount;
		this.amount = this.credit-(this.carraige+this.loading+this.unloading-this.discount);
	}



	public CustomerStatement(Double debit,String reference,LocalDate date,String transactionNumber) {
		super();
		this.date = date;
		this.debit = debit;
		this.reference=reference+"-"+transactionNumber;
		this.type=Constants.DAYBOOK;
	}
	
	public CustomerStatement(String reference,Double credit,LocalDate date,String transactionNumber) {
		super();
		this.date = date;
		this.credit = credit;
		this.reference=reference+"-"+transactionNumber;
		this.type=Constants.DAYBOOK;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getDate() {
		return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLoading() {
		return loading;
	}

	public void setLoading(Double loading) {
		this.loading = loading;
	}

	public Double getUnloading() {
		return unloading;
	}

	public void setUnloading(Double unloading) {
		this.unloading = unloading;
	}

	public Double getCarraige() {
		return carraige;
	}

	public void setCarraige(Double carraige) {
		this.carraige = carraige;
	}

	public Double getDebit() {
		return debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public Double getBalance() {
		if(balance !=null)
			balance = Double.valueOf(df.format(balance));
		return balance;
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

	public List<SalesStatement> getSales() {
		return sales;
	}

	public void setSales(List<SalesStatement> sales) {
		this.sales = sales;
	}



	public Double getAmount() {
		return amount;
	}



	public void setAmount(Double amount) {
		this.amount = amount;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public Double getDiscount() {
		return discount;
	}



	public void setDiscount(Double discount) {
		this.discount = discount;
	}



	
	
	
	
}
