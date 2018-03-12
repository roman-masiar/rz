package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.entity.PK;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.ProductCategory;



@Service
@EnableCaching
public class ProductCategoryDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (ProductCategory productCategory) {
		 dao.saveOrUpdate(productCategory);
	}
	
	

	public ProductCategory get(PK id) {
		
		return dao.get(ProductCategory.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<ProductCategory> getAll(int companyId) {
		return (List<ProductCategory>) dao.query("from ProductCategory where companyId=:companyId", 
				oneParam("companyId", companyId));
	}
	
	

	public void delete(int id) {
		
		dao.execute("delete from ProductCategory where id=:id", oneParam("id", id));
	}



	
	
	

}
