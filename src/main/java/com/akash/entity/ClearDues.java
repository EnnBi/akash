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
@Table(name="clear_dues")
public class ClearDues {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	AppUser user;
	
	@Column(name="clear_amount")
	Double amount;
	
	@Column(name="date")
	@DateTimeFormat(pattern="dd-MM-yyyy")
	LocalDate date;
	
	@ManyToOne
	@JoinColumn(name="owner_id")
	AppUser ownerName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public AppUser getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(AppUser ownerName) {
		this.ownerName = ownerName;
	}
	
	
	

}
