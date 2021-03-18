package com.akash.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.akash.entity.MaterialType;

public interface MaterialTypeRepository extends CrudRepository<MaterialType, Long>,PagingAndSortingRepository<MaterialType, Long> {
	
	public boolean existsByName(String name);
	
	@Query("select m from MaterialType m where m.name=:name and m.id!=:id")
	public MaterialType checkMaterialAlreadyExists(@Param("name") String name,@Param("id") long id);

}
