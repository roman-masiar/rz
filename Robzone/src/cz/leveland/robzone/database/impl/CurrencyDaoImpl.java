package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Currency;



@Service
@EnableCaching
public class CurrencyDaoImpl extends DaoImpl {
	
	
	

	public void saveOrUpdate (Currency curr) {
		 dao.saveOrUpdate(curr);
		 
	}
	
	public Currency get(int id) {
		
		return dao.get(Currency.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Currency> getAll() {
		
		return (List<Currency>) dao.query("from Currency", null);
	}
	


	
	
	

}
