package com.akash.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.akash.entity.ClearDues;
import com.akash.entity.CustomerStatement;

public interface ClearDuesRepository extends CrudRepository<ClearDues, Long>,PagingAndSortingRepository<ClearDues,Long> {
	
	@Query("Select sum(d.amount) from ClearDues d where d.user.id = :id and (d.date Between :startDate and :endDate)")
	Double findUserClearDues(@Param("id") long id,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

	@Query("Select new com.akash.entity.CustomerStatement(d.amount,d.date,d.ownerName.name) from ClearDues d where d.user.id = :id and (d.date Between :startDate and :endDate)")
	List<CustomerStatement> findCustomerClearDuesBetweenDates(@Param("id") long id,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
}
