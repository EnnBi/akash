package com.akash.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="app_user")
public class AppUser {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@NotNull(message ="name is required")
	@NotEmpty(message="name is required")
	@Column(name="name")
	private String name;
	
	@Pattern(regexp = "^\\d{10}$", message = "Please Enter the 10 digits correctly")
	@NotNull(message ="contact is required")
	@NotEmpty(message="contact is required")
	@Column(name="contact")
	private String contact;
	
	@Pattern(regexp = "^\\d{10}$", message = "Please Enter the 10 digits correctly")
	@Column(name="contact_2")
	private String contactTwo;
	
	@Pattern(regexp = "^\\d{10}$", message = "Please Enter the 10 digits correctly")
	@Column(name="contact_3")
	private String contactThree;
	
	@NotNull(message ="address is required")
	@NotEmpty(message="address is required")
	@Column(name="address")
	private String address;
	
	@ManyToOne
	@JoinColumn(name="user_type_id")
	@NotNull(message ="select any userType")
	UserType userType;
	
	@Column(name="Code")
	String code;
	
	@NotNull(message ="Ledger Number is required")
	@NotEmpty(message="Ledger Number is required")
	@Column(name="ledger_number")
	private String ledgerNumber;
	
	@Column(name="account_number")
	private String accountNumber;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="site")
	List<Site> sites;
	
	
	@ManyToOne
	@JoinColumn(name="labour_group")
	LabourGroup labourGroup;
	
	@Column(name="Active")
	boolean active;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public UserType getUserType() {
		return userType;
	}
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	public String getLedgerNumber() {
		return ledgerNumber;
	}
	public void setLedgerNumber(String ledgerNumber) {
		this.ledgerNumber = ledgerNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public List<Site> getSites() {
		return sites;
	}
	public void setSites(List<Site> sites) {
		this.sites = sites;
	}
	
	public LabourGroup getLabourGroup() {
		return labourGroup;
	}
	public void setLabourGroup(LabourGroup labourGroup) {
		this.labourGroup = labourGroup;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLabelName(){
		return this.name+" "+this.address;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
	public String getContactTwo() {
		return contactTwo;
	}
	public void setContactTwo(String contactTwo) {
		this.contactTwo = contactTwo;
	}
	public String getContactThree() {
		return contactThree;
	}
	public void setContactThree(String contactThree) {
		this.contactThree = contactThree;
	}
	@Override
	public String toString() {
		return "AppUser [id=" + id + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppUser other = (AppUser) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

}
