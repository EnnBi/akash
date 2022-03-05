package com.akash.entity;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="goods_return")
public class GoodsReturn {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	AppUser customer;

	@ManyToOne
	@JoinColumn(name = "site")
	Site site;

	@ManyToOne
	@JoinColumn(name = "vehicle")
	Vehicle vehicle;

	@Column(name = "reciept_number")
	private String receiptNumber;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "Date")
	private LocalDate date;

	@Column(name = "unit")
	private Double unit;

	@Column(name = "loading_amount")
	private Double loadingAmount;

	@Column(name = "unloading_amount")
	private Double unloadingAmount;

	@Column(name = "total")
	private Double total;

	

	@OneToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "Goods_Return")
	List<Sales> sales;

	@Column(name = "carriage")
	private Double carraige;

	@Column(name = "Loading_Amount_Per_Head")
	private Double loadingAmountPerHead;

	@Column(name = "Unloading_Amount_Per_Head")
	private Double unloadingAmountPerHead;

	@Column(name = "Sites")
	String sites;

	@Column(name = "Driver_Loading_Charges")
	Double driverLoadingCharges;

	@Column(name = "Driver_Unloading_Charges")
	Double driverUnloadingCharges;

	@Column(name = "Vehicle_Other")
	String otherVehicle;

	@ManyToOne
	AppUser driver;

	@ManyToOne
	LabourGroup labourGroup;
	
	@ManyToOne
	LabourGroup unloaderLabourGroup;
	
	@Transient
	DecimalFormat df = new DecimalFormat("#.##"); 

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AppUser getCustomer() {
		return customer;
	}

	public void setCustomer(AppUser customer) {
		this.customer = customer;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getUnit() {
		return unit;
	}

	public void setUnit(Double unit) {
		this.unit = unit;
	}

	public Double getLoadingAmount() {
		return loadingAmount;
	}

	public void setLoadingAmount(Double loadingAmount) {
		this.loadingAmount = loadingAmount;
	}

	public Double getUnloadingAmount() {
		return unloadingAmount;
	}

	public void setUnloadingAmount(Double unloadingAmount) {
		this.unloadingAmount = unloadingAmount;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	

	public List<Sales> getSales() {
		return sales;
	}

	public void setSales(List<Sales> sales) {
		this.sales = sales;
	}

	public Double getCarraige() {
		return carraige;
	}

	public void setCarraige(Double carraige) {
		this.carraige = carraige;
	}

	public Double getLoadingAmountPerHead() {
		return loadingAmountPerHead;
	}

	public void setLoadingAmountPerHead(Double loadingAmountPerHead) {
		if(loadingAmountPerHead != null) {
		this.loadingAmountPerHead = Double.valueOf(df.format(loadingAmountPerHead));
		}
		else {
			this.loadingAmountPerHead=loadingAmountPerHead;
		}
	}

	public Double getUnloadingAmountPerHead() {
		return unloadingAmountPerHead;
	}

	public void setUnloadingAmountPerHead(Double unloadingAmountPerHead) {
		if(unloadingAmountPerHead !=null) {
		this.unloadingAmountPerHead = Double.valueOf(df.format(unloadingAmountPerHead));
		}
		else {
			this.unloadingAmountPerHead = unloadingAmountPerHead;
		}
	}

	public String getSites() {
		return sites;
	}

	public void setSites(String sites) {
		this.sites = sites;
	}

	public String getOtherVehicle() {
		return otherVehicle;
	}

	public void setOtherVehicle(String otherVehicle) {
		this.otherVehicle = otherVehicle;
	}

	public LabourGroup getLabourGroup() {
		return labourGroup;
	}

	public void setLabourGroup(LabourGroup labourGroup) {
		this.labourGroup = labourGroup;
	}

	public Double getDriverLoadingCharges() {
		return driverLoadingCharges;
	}

	public void setDriverLoadingCharges(Double driverLoadingCharges) {
		if(driverLoadingCharges != null) {
		this.driverLoadingCharges = Double.valueOf(df.format(driverLoadingCharges));
		}
		else {
			this.driverLoadingCharges=driverLoadingCharges;
		}
	}

	public Double getDriverUnloadingCharges() {
		return driverUnloadingCharges;
	}

	public void setDriverUnloadingCharges(Double driverUnloadingCharges) {
		if(driverUnloadingCharges != null) {
		this.driverUnloadingCharges = Double.valueOf(df.format(driverUnloadingCharges));
		}
		else {
			this.driverUnloadingCharges=driverUnloadingCharges;
			
		}
	}

	public AppUser getDriver() {
		return driver;
	}

	public void setDriver(AppUser driver) {
		this.driver = driver;
	}
		
	
	public LabourGroup getUnloaderLabourGroup() {
		return unloaderLabourGroup;
	}

	public void setUnloaderLabourGroup(LabourGroup unloaderLabourGroup) {
		this.unloaderLabourGroup = unloaderLabourGroup;
	}

}
