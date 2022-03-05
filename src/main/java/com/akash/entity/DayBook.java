package com.akash.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="day_book")
public class DayBook {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name="customerId")
	AppUser user;
	
	@Column(name="transaction_number")
	private String transactionNumber;
	
	@Column(name="transaction_type")
	private String transactionType;
	
	@Column(name="Transaction_By")
	private String transactionBy;
	
	@Column(name="account_number")
	private String accountNumber;
	
	@Column(name="responsible_person")
	private String responsiblePerson;
	
	@Column(name="Amount")
	private Double amount;
	
	@Column(name="date")
	@DateTimeFormat(pattern="dd-MM-yyyy")
	LocalDate date;
	
	@Column(name="status")
	private String status;
	
	@Column(name="description")
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public String getTransactionBy() {
		return transactionBy;
	}

	public void setTransactionBy(String transactionBy) {
		this.transactionBy = transactionBy;
	}

	
	
	
	

}
