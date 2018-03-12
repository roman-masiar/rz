package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.VatCategory;



@Service
@EnableCaching
public class VatCategoryDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (VatCategory productCategory) {
		 dao.saveOrUpdate(productCategory);
	}
	
	

	public VatCategory get(int id) {
		
		return dao.get(VatCategory.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<VatCategory> getAll(int companyId) {
		return (List<VatCategory>) dao.query("from VatCategory where companyId=:companyId", 
				oneParam("companyId", companyId));
	}
	
	

	public void delete(int id) {
		
		dao.execute("delete from VatCategory where id=:id", oneParam("id", id));
	}



	
	
	

}
