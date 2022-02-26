package com.akash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.akash.entity.AppUser;
import com.akash.entity.Site;
import com.akash.projections.AppUserProjection;
import com.akash.repository.custom.AppUserCustomizedRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Long>,AppUserCustomizedRepository {
	
	public boolean existsByContact(String contact);
	
	@Query("select a from AppUser a where a.contact=:contact and a.id!=:id")
	public AppUser chechUserExistsAlready(@Param("contact") String Contact,@Param("id") long id);
	
	public List<AppUser> findByUserType_NameAndActive(String userType,boolean active);
	
	@Query("Select a from AppUser a where a.userType.name in :userType and a.active = :active")
	public List<AppUser> findAppUsersOnType(@Param("userType") String[] userType,@Param("active") boolean active);
	
	
	public List<AppUserProjection> findByUserType_NameInAndActive(String [] userTypes,boolean active);

	@Query("Select u.sites from AppUser u where u.id= :id")
	List<Site> findSitesOnUserId(@Param("id") long id);
	
	List<AppUser> findByUserType_NameAndLabourGroup_IdAndActive(String userType,long id,boolean active);
	
	public List<AppUserProjection> findByUserType_IdAndActive(long id,boolean active);
	
	@Query("Select a from AppUser a where a.userType.name in :userType")
	public List<AppUser> findAllAppUsersOnType(@Param("userType") String[] userType);
	
	
	
	
}
 