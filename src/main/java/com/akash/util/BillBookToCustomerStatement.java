package com.akash.util;

import java.util.ArrayList;
import java.util.List;

import com.akash.entity.BillBook;
import com.akash.entity.CustomerStatement;

public class BillBookToCustomerStatement {

	public static List<CustomerStatement> convert(List<BillBook> billBooks) {
		List<CustomerStatement> customerStatements =  new ArrayList<>();
		billBooks.forEach(b->{
		 Double	discount=b.getDiscount()==null?Double.valueOf(0):b.getDiscount();
			b.setTotal(b.getTotal()-discount);
			CustomerStatement customerStatement = new CustomerStatement(b.getId(), b.getReceiptNumber(), b.getDate(), b.getVehicle(),b.getOtherVehicle(),b.getCustomer().getAddress(), b.getSites(), b.getLoadingAmount(),b.getUnloadingAmount(), b.getCarraige(),b.getDiscount(), b.getTotal());
			customerStatements.add(customerStatement);
		});
		return customerStatements;
	}
}
