package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.entity.PK;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.ProductType;
import cz.leveland.robzone.database.entity.UnknownProduct;
import cz.leveland.robzone.database.entity.dto.OrderDto;
import cz.leveland.robzone.database.entity.dto.ProductTypeDto;



@Service
@EnableCaching
public class UnknownProductDaoImpl extends DaoImpl {
	

	public void flushAndClear () {
		dao.flushAndClear();
	}
	public void saveOrUpdate (UnknownProduct up) {
		 dao.saveOrUpdate(up);
		 
	}
	
	

	public UnknownProduct get(PK id) {
		
		return dao.get(UnknownProduct.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<UnknownProduct> getAll(int companyId) {
		return (List<UnknownProduct>) dao.query("from UnknownProduct where companyId=:companyId", oneParam("companyId", companyId));
	}
	
	
	public void deleteAll(int companyId) {
		
		dao.execute(
				"delete from UnknownProduct where companyid=:companyId",								 
				oneParam("companyId", companyId));
	}
	

}
