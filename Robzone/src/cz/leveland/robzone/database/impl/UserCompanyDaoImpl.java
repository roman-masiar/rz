package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Company;
import cz.leveland.robzone.database.entity.UserCompany;



@Service
@EnableCaching
public class UserCompanyDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (UserCompany uc) {
		 dao.saveOrUpdate(uc);
	}
	
	

	public UserCompany get(int id) {
		
		return dao.get(UserCompany.class, id);
	}
	

	/*
	@SuppressWarnings("unchecked")
	public List<UserCompany> getAll(int userId) {
		return (List<UserCompany>) dao.query("from UserCompany where userId=:userId order by id", 
				oneParam("userId", userId));
	}
	*/
	
	@SuppressWarnings("unchecked")
	public List<Company> getAll(int userId) {
		return (List<Company>) dao.query(
				"select comp "
				+ "from UserCompany uc, Company comp "
				+ "where uc.companyId=comp.id and uc.userId=:userId "
				+ "order by uc.id", 
				oneParam("userId", userId));
	}
	
	@SuppressWarnings("unchecked")
	public List<UserCompany> getByUserId(int userId) {
		return (List<UserCompany>) dao.query("from UserCompany where userId=:userId", 
				oneParam("userId", userId));
	}
	
	

	public void delete(int id) {
		
		dao.execute("delete from UserCompany where id=:id", oneParam("id", id));
	}



	
	
	

}
