package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.ProductSetItem;
import cz.leveland.robzone.database.entity.dto.ProductSetItemDto;



@Service
@EnableCaching
public class ProductSetItemDaoImpl extends DaoImpl {
	
	
	

	public void saveOrUpdate (ProductSetItem productSetItem) {
		 dao.saveOrUpdate(productSetItem);
		 
	}
	
	
	public ProductSetItem get(int id) {
		
		return dao.get(ProductSetItem.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ProductSetItemDto> getAll(int companyId) {
		return (List<ProductSetItemDto>) dao.querySQLTransform("select id as id, productid as productId, stockItemId as stockItemId, qty as qty "
				+ "from v_product_sets "
				+ "where companyId=:companyId", oneParam("companyId", companyId), ProductSetItemDto.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductSetItemDto> getByProductId(int productId) {
		return (List<ProductSetItemDto>) dao.queryTransform(
				"select psi.id as id, psi.productId as productId, psi.stockItemId as stockItemId, psi.qty as qty, "
				+ "si.name as name, si.hasSerialNo as hasSerialNo "
				+ "from ProductSetItem psi, StockItem si "
				+ "where psi.stockItemId=si.id and productId=:productId", 
				oneParam("productId", productId), ProductSetItemDto.class);
	}
	

	public void delete(int orderId) {
		
		dao.execute("delete from ProductSetItem where id=:id", oneParam("id", orderId));
		
	}


	public void deleteByProduct(Integer productId) {
		dao.execute("delete from ProductSetItem where productId=:productId", oneParam("productId", productId));		
	}



	
	
	

}
