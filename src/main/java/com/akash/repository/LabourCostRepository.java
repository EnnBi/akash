package com.akash.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.akash.entity.LabourCost;
import com.akash.entity.LabourGroup;
import com.akash.entity.Product;
import com.akash.entity.Size;
import com.akash.repository.projections.LabourCostProj;

public interface LabourCostRepository extends CrudRepository<LabourCost, Long>,PagingAndSortingRepository<LabourCost, Long>  {
	
	boolean existsByProductAndLabourGroupAndSize(Product product,LabourGroup labourGroup,Size size);

	boolean existsByProductAndLabourGroupAndSizeAndIdNot(Product product, LabourGroup labourGroup, Size size, long id);
	
	@Query("Select l.rate from LabourCost l where l.product.id=:product and l.size.id=:size and l.labourGroup.id=:lg")
	Double findRate(@Param("product") long product,@Param("size") long size,@Param("lg") long labourGroup);
	
	LabourCostProj findByProduct_IdAndSize_IdAndLabourGroup_Id(Long productId,Long sizeId,Long labourGroupId);
	
}
