package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.entity.PK;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.ProductType;
import cz.leveland.robzone.database.entity.dto.ProductTypeDto;



@Service
@EnableCaching
public class ProductTypeDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (ProductType productType) {
		 dao.saveOrUpdate(productType);
		 
	}
	
	

	public ProductType get(PK id) {
		
		return dao.get(ProductType.class, id);
	}
	

	@SuppressWarnings("unchecked")
	@Cacheable("producttype")
	public List<ProductTypeDto> getAll(int companyId, boolean nonGoodsOnly) {
		
		System.out.println("Product dao: getNonGoodsTypes " + companyId);
		
		String ngo = nonGoodsOnly ? "and npt.productTypeId > 1" : "";
		
		return (List<ProductTypeDto>) dao.querySQLTransform(
				"select npt.id as id, npt.code as code, npt.productTypeId as productTypeId, npt.productNatureId as productNatureId, npt.productName "
				+ "from v_nonproduct_types npt "
				+ "where npt.companyId=:companyId " + ngo, 
				oneParam("companyId", companyId), ProductTypeDto.class);
	}
	
	
	@SuppressWarnings("unchecked")
	@Cacheable("producttype")
	public List<ProductTypeDto> getAll(int companyId) {
		
		return (List<ProductTypeDto>) dao.querySQLTransform(
				"select id as id, name as name, productnatureid as productNatureId "
						+ "from v_product_types "
						+ "where companyId=:companyId " , 
						oneParam("companyId", companyId), ProductTypeDto.class);
	}
	
	

	public void delete(int id) {
		
		dao.execute("delete from ProductType where id=:id", oneParam("id", id));
		
	}



	
	
	

}
