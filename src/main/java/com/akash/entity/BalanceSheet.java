package com.akash.entity;

import java.util.Objects;

import com.akash.util.CommonMethods;

public class BalanceSheet {

	AppUser user;
	Double balance;
	Double debit;
	Double credit;

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public Double getBalance() {
		return CommonMethods.format(balance);
	}

	public void setBalance(Double balance) {
		this.balance = Objects.isNull(balance) ? Double.valueOf(0) : balance;
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
}
