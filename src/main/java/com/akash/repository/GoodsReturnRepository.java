package com.akash.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.akash.entity.CustomerStatement;
import com.akash.entity.GoodsReturn;
import com.akash.repository.custom.GoodsReturnCustomizedRepository;

public interface GoodsReturnRepository extends CrudRepository<GoodsReturn, Long>,PagingAndSortingRepository<GoodsReturn,Long>,GoodsReturnCustomizedRepository {
	
	@Query("Select sum(d.total) from GoodsReturn d where d.customer.id = :id and (d.date Between :startDate and :endDate)")
	Double findUserGoodsReturn(@Param("id") long id,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	@Query("Select new com.akash.entity.CustomerStatement(d.total,d.date,d.receiptNumber) from GoodsReturn d where d.customer.id = :id and (d.date Between :startDate and :endDate)")
	List<CustomerStatement> findCustomerGoodsReturnBetweenDates(@Param("id") long id,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

}
