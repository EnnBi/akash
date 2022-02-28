package com.akash.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="sales")
public class Sales {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	Product product;
	
	@ManyToOne
	@JoinColumn(name="size")
	Size size;
	
	@Column(name="quantity")
	private Double quantity;
	
	@Column(name="unit_price")
	private Double unitPrice;
	
	@Column(name="amount")
	private Double amount;
	
	@ManyToOne
	@JoinColumn(name="bill_book")
	BillBook billBook;
	
	@ManyToOne
	@JoinColumn(name="good_return")
	GoodsReturn goodsReturn;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public BillBook getBillBook() {
		return billBook;
	}

	public void setBillBook(BillBook billBook) {
		this.billBook = billBook;
	}
	

	public GoodsReturn getGoodsReturn() {
		return goodsReturn;
	}

	public void setGoodsReturn(GoodsReturn goodsReturn) {
		this.goodsReturn = goodsReturn;
	}

	@Override
	public String toString() {
		return "Sales [id=" + id + ", product=" + product + ", size=" + size + ", quantity=" + quantity + ", unitPrice="
				+ unitPrice + ", amount=" + amount + "]";
	}
	
	

}
