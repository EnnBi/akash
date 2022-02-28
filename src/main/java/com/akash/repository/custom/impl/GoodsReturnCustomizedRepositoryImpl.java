package com.akash.repository.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.akash.entity.BillBook;
import com.akash.entity.BillBookSearch;
import com.akash.entity.GoodsReturn;
import com.akash.entity.GoodsReturnSearch;
import com.akash.entity.dto.BillBookDTO;
import com.akash.repository.custom.GoodsReturnCustomizedRepository;
import com.akash.util.Constants;

public class GoodsReturnCustomizedRepositoryImpl implements GoodsReturnCustomizedRepository{
	
	
	  @PersistenceContext 
	  EntityManager em;
	 

	@Override
	public GoodsReturn billBookToGoodsReturnMapping(BillBook billbook) {
		GoodsReturn goodsReturn= new GoodsReturn();
		goodsReturn.setCarraige(null);
		goodsReturn.setCustomer(billbook.getCustomer());
		goodsReturn.setDate(null);
		goodsReturn.setDriver(null);
		goodsReturn.setDriverLoadingCharges(null);
		goodsReturn.setDriverUnloadingCharges(null);
		goodsReturn.setLabourGroup(null);
		goodsReturn.setLoadingAmount(null);
		goodsReturn.setLoadingAmountPerHead(null);
		goodsReturn.setOtherVehicle(null);
		goodsReturn.setReceiptNumber(billbook.getReceiptNumber());
		goodsReturn.setSales(billbook.getSales());
		goodsReturn.setSite(null);
		goodsReturn.setSites(null);
		goodsReturn.setTotal(billbook.getTotal()-billbook.getLoadingAmount()-billbook.getUnloadingAmount()-billbook.getCarraige());
		goodsReturn.setUnit(billbook.getUnit());
		goodsReturn.setUnloaderLabourGroup(null);
		goodsReturn.setUnloadingAmount(null);
		goodsReturn.setUnloadingAmountPerHead(null);
		goodsReturn.setVehicle(null);
		return goodsReturn;
	}

	@Override
	public List<GoodsReturn> searchPaginated(GoodsReturnSearch goodsReturnSearch, int from) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GoodsReturn> cq = cb.createQuery(GoodsReturn.class);
		Root<GoodsReturn> root = cq.from(GoodsReturn.class);

		List<Predicate> predicates = getPredicates(cb, root, goodsReturnSearch);
		cq.select(root)
				.where(predicates.toArray(new Predicate[] {}));
		
		cq.orderBy(cb.desc(root.get("date")));
		cq.distinct(true);
		return em.createQuery(cq).setFirstResult(from).setMaxResults(Constants.ROWS).getResultList();
	}

	@Override
	public long count(GoodsReturnSearch goodsReturnSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<GoodsReturn> root = cq.from(GoodsReturn.class);

		List<Predicate> predicates = getPredicates(cb, root, goodsReturnSearch);
		cq.select(cb.count(root))
				.where(predicates.toArray(new Predicate[] {}));
		
		cq.orderBy(cb.desc(root.get("date")));
		cq.distinct(true);
		try {
			return em.createQuery(cq).getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	public List<Predicate> getPredicates(CriteriaBuilder cb, Root<GoodsReturn> root, GoodsReturnSearch goodsReturnSearch) {
		List<Predicate> predicates = new ArrayList<>();

		if (isNotNullOrNotEmpty(goodsReturnSearch.getReceiptNumber()))
			predicates.add(cb.equal(root.get("receiptNumber"), goodsReturnSearch.getReceiptNumber()));

		if (goodsReturnSearch.getCustomerId() != null)
			predicates.add(cb.equal(root.get("customer").get("id"), goodsReturnSearch.getCustomerId()));
		

		predicates.add(cb.between(root.get("date"), goodsReturnSearch.getStartDate(), goodsReturnSearch.getEndDate()));

		return predicates;
	}
	
	public boolean isNotNullOrNotEmpty(String string) {
		if (string != null)
			if (!string.isEmpty())
				return true;

		return false;
	}

}
