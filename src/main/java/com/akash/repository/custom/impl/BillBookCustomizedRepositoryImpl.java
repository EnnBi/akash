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
import com.akash.entity.dto.BillBookDTO;
import com.akash.repository.custom.BillBookCustomizedRepository;
import com.akash.util.Constants;

public class BillBookCustomizedRepositoryImpl implements BillBookCustomizedRepository {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<BillBookDTO> searchPaginated(BillBookSearch billBookSearch,int from) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<BillBookDTO> cq = cb.createQuery(BillBookDTO.class);
		Root<BillBook> root = cq.from(BillBook.class);

		List<Predicate> predicates = getPredicates(cb, root, billBookSearch);
		cq.select(cb.construct(BillBookDTO.class, root.get("id"), root.get("receiptNumber"),
				root.get("customer").get("name"), root.get("customer").get("address"), root.get("date"),
				root.get("sites"), root.get("total")))
				.where(predicates.toArray(new Predicate[] {}));
		
		cq.orderBy(cb.desc(root.get("receiptNumber")));
		cq.distinct(true);
		return em.createQuery(cq).setFirstResult(from).setMaxResults(Constants.ROWS).getResultList();
	}

	public List<Predicate> getPredicates(CriteriaBuilder cb, Root<BillBook> root, BillBookSearch billBookSearch) {
		List<Predicate> predicates = new ArrayList<>();

		if (isNotNullOrNotEmpty(billBookSearch.getReceiptNumber()))
			predicates.add(cb.equal(root.get("receiptNumber"), billBookSearch.getReceiptNumber()));

		if (billBookSearch.getCustomerId() != null)
			predicates.add(cb.equal(root.get("customer").get("id"), billBookSearch.getCustomerId()));
		if (billBookSearch.getVehicleId() != null)
			predicates.add(cb.equal(root.get("vehicle").get("id"), billBookSearch.getVehicleId()));
		if (billBookSearch.getSiteId() != null)
			predicates.add(cb.equal(root.get("site").get("id"), billBookSearch.getSiteId()));

		predicates.add(cb.between(root.get("date"), billBookSearch.getStartDate(), billBookSearch.getEndDate()));

		return predicates;
	}

	
	@Override
	public long count(BillBookSearch billBookSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<BillBook> root = cq.from(BillBook.class);

		List<Predicate> predicates = getPredicates(cb, root, billBookSearch);
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
	
	public boolean isNotNullOrNotEmpty(String string) {
		if (string != null)
			if (!string.isEmpty())
				return true;

		return false;
	}

}
