package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Company;



@Service
@EnableCaching
public class CompanyDaoImpl extends DaoImpl {
	
	
	

	public void saveOrUpdate (Company company) {
		 dao.saveOrUpdate(company);
		 
	}
	
	
	public Company get(int id) {
		
		return dao.get(Company.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Company> getAll() {
		
		return (List<Company>) dao.query("from Company", null);
	}
	


	
	
	

}
