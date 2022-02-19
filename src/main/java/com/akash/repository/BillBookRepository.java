package com.akash.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.akash.entity.AppUser;
import com.akash.entity.BillBook;
import com.akash.entity.CustomerStatement;
import com.akash.entity.DriverStatement;
import com.akash.entity.LabourStatement;
import com.akash.entity.SalesStatement;
import com.akash.repository.custom.BillBookCustomizedRepository;
import com.akash.util.Constants;

public interface BillBookRepository extends CrudRepository<BillBook,Long>,BillBookCustomizedRepository{
	
	boolean existsByReceiptNumber(String number);
	
	@Query("Select Sum(b.carraige) from BillBook b where b.driver.id = :id and b.date between :startDate and :endDate")
	Double sumOfCarraige(@Param("id") long driver,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	@Query("Select Sum(b.driverLoadingCharges) from BillBook b where :driver MEMBER OF b.loaders and b.date between :startDate and :endDate")
	Double sumOfDriverLoading(@Param("driver") AppUser driver,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	@Query("Select Sum(b.driverUnloadingCharges) from BillBook b where :driver MEMBER OF b.unloaders and b.date between :startDate and :endDate")
	Double sumOfDriverUnloading(@Param("driver") AppUser driver,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	@Query("Select new com.akash.entity.DriverStatement(b.date,b.receiptNumber,b.customer.name,b.sites,b.carraige,b.driverLoadingCharges,b.driverUnloadingCharges,'"+Constants.BILLBOOK+"')  from BillBook b where (:driver MEMBER OF b.loaders OR :driver MEMBER OF b.unloaders OR b.driver = :driver ) AND (b.date between :startDate and :endDate)")
	List<DriverStatement> findDriverDebits(@Param("driver") AppUser driver,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	@Query("Select Sum(b.loadingAmountPerHead) from BillBook b where :labour MEMBER OF b.loaders and b.date between :startDate and :endDate")
	Double sumOfLabourLoading(@Param("labour") AppUser labour,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	@Query("Select Sum(b.unloadingAmountPerHead) from BillBook b where :labour MEMBER OF b.unloaders and b.date between :startDate and :endDate")
	Double sumOfLabourUnloading(@Param("labour") AppUser labour,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

	@Query("Select new com.akash.entity.LabourStatement(b.date,b.loadingAmountPerHead,b.unloadingAmountPerHead,b.receiptNumber,'"+Constants.BILLBOOK+"') from BillBook b where (:labour Member Of b.loaders) and  (b.date between :startDate and :endDate)")
	List<LabourStatement> findLabourBillBookDebits(@Param("labour") AppUser labour,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

	@Query("Select Sum(b.total) from BillBook b where b.customer.id = :id and b.date between :startDate and :endDate")
	Double sumOfCustomerDebits(@Param("id") long id,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	@Query("Select Sum(b.discount) from BillBook b where b.customer.id = :id and b.date between :startDate and :endDate")
	Double sumOfCustomerDiscounts(@Param("id") long id,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	 
	@Query("Select new com.akash.entity.CustomerStatement(b.id,b.receiptNumber,b.date,b.vehicle,b.otherVehicle,b.customer.address,b.sites,b.loadingAmount,b.unloadingAmount,b.carraige,b.discount,b.total) from BillBook b where b.customer.id = :id and (b.date between :startDate and :endDate)")
	List<CustomerStatement> findCustomerBillBookDebits(@Param("id") long id,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	 
	@Query("Select new  com.akash.entity.SalesStatement(s.product.name,s.size.name,s.quantity,s.unitPrice,s.amount) from Sales s where s.billBook.id = :id")
	List<SalesStatement> findSalesOnBillBookId(@Param("id") long id);
	
	@Query("Select Sum(s.quantity) from Sales s where s.product.id = :product and s.size.id = :size and (s.billBook.date between :startDate and :endDate)")
	Double findSumOfSold(@Param("product") long product,@Param("size") long size,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	List<BillBook> findByCustomer_IdAndDateBetween(long id,LocalDate startDate,LocalDate endDate);
} 
  