package com.akash.repository.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.akash.entity.AppUser;
import com.akash.entity.AppUserSearch;
import com.akash.repository.custom.AppUserCustomizedRepository;
import com.akash.util.Constants;

public class AppUserCustomizedRepositoryImpl implements AppUserCustomizedRepository{
	
	@PersistenceContext
	EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AppUser> searchAppUserPaginated(AppUserSearch appUserSearch,int from) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AppUser> cq = cb.createQuery(AppUser.class);
		Root<AppUser> root = cq.from(AppUser.class);
		List<Predicate> predicates=getPredicates(root, cb, appUserSearch);
		
		cq.select(root).where(predicates.toArray(new Predicate[] {}));
		cq.distinct(true);
		cq.orderBy(cb.asc(root.get("name")));

		Query query=em.createQuery(cq);
		
		return query.setFirstResult(from).setMaxResults(Constants.ROWS).getResultList();

}
	public List<Predicate> getPredicates(Root<AppUser> root,CriteriaBuilder cb,AppUserSearch appUserSearch){
		List<Predicate> predicates=new ArrayList<>();
		
		if(isNotNullOrNotEmpty(appUserSearch.getAccountNumber()))
			predicates.add(cb.equal(root.get("accountNumber"), appUserSearch.getAccountNumber()));
		
		if(isNotNullOrNotEmpty(appUserSearch.getLedgerNumber()))
			predicates.add(cb.equal(root.get("ledgerNumber"),appUserSearch.getLedgerNumber()));
		
		if(isNotNullOrNotEmpty(appUserSearch.getContact()))
			predicates.add(cb.or(cb.equal(root.get("contact"),appUserSearch.getContact()),cb.equal(root.get("contactTwo"),appUserSearch.getContact()),cb.equal(root.get("contactThree"),appUserSearch.getContact())));
		
		if(isNotNullOrNotEmpty(appUserSearch.getName()))
			predicates.add(cb.like(root.get("name"),"%"+appUserSearch.getName()+"%"));
		
		if(isNotNull(appUserSearch.getUserTypeId()))
			predicates.add(cb.equal(root.get("userType").get("id"),appUserSearch.getUserTypeId()));
		
		if(isNotNull(appUserSearch.getLabourGroupId()))
			predicates.add(cb.equal(root.get("labourGroup").get("id"),appUserSearch.getLabourGroupId()));
		return predicates;
	}
	
	@Override
	public long searchAppUsersCount(AppUserSearch appUserSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<AppUser> root = cq.from(AppUser.class);
		
		List<Predicate> predicates=getPredicates(root, cb, appUserSearch);
		
		cq.distinct(true);
		cq.multiselect(cb.count(root)).where(predicates.toArray(new Predicate[] {}));
		
	//	cq.orderBy(cb.desc(root.get("date")));

		Query query=em.createQuery(cq);
		
		try {
			return (long) query.getSingleResult();
		} catch (Exception e) {
			return 0;
		}
	}
	public boolean isNotNull(Long	 lng){
		if(lng!=null)
		return true;
		
		return false;
	}
	
	public boolean isNotNullOrNotEmpty(String str){
		if(str!=null)
			if(!str.isEmpty())
				return true;
		
		return false;
	}

}
	
	



