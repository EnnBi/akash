package com.akash.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;



@Entity
public class LabourCost {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	long id;
	
	@Column(name="Rate")
	Double rate;
	
	@ManyToOne
	@JoinColumn(name="Labour_Group")
	LabourGroup labourGroup;
	
	@ManyToOne
	@JoinColumn(name="Product")
	Product product;
	
	@ManyToOne
	@JoinColumn(name="Size")
	Size size;
	
	@Column(name = "Loading_Rate")
	private Double loadingRate;

	@Column(name = "Unloading_Rate")
	private Double unloadingRate;

	
	public LabourCost() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public LabourGroup getLabourGroup() {
		return labourGroup;
	}

	public void setLabourGroup(LabourGroup labourGroup) {
		this.labourGroup = labourGroup;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public Double getLoadingRate() {
		return loadingRate;
	}

	public void setLoadingRate(Double loadingRate) {
		this.loadingRate = loadingRate;
	}

	public Double getUnloadingRate() {
		return unloadingRate;
	}

	public void setUnloadingRate(Double unloadingRate) {
		this.unloadingRate = unloadingRate;
	}
	
	
}
