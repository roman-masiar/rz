package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.entity.PK;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Country;



@Service
@EnableCaching
public class CountryDaoImpl extends DaoImpl {
	
	
	

	public void saveOrUpdate (Country country) {
		 dao.saveOrUpdate(country);
		 
	}
	
	public Country get(int id) {
		
		return dao.get(Country.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Country> getAll(int companyId) {
		
		return (List<Country>) dao.query(
				"from Country where companyId=:companyId", 
				oneParam("companyId", companyId));
	}
	


	
	
	

}
