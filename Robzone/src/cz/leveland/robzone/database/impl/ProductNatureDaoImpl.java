package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.entity.PK;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.ProductNature;



@Service
@EnableCaching
public class ProductNatureDaoImpl extends DaoImpl {
	
	
	

	public void saveOrUpdate (ProductNature pn) {
		 dao.saveOrUpdate(pn);
		 
	}
	
	public ProductNature get(PK id) {
		
		return dao.get(ProductNature.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ProductNature> getAll(int companyId) {
		
		
		return (List<ProductNature>) dao.query(
				"from ProductNature where companyId=:companyId", 
				oneParam("companyId", companyId));
	}
	


	
	
	

}
