package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.entity.PK;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.StockSetItem;
import cz.leveland.robzone.database.entity.dto.StockSetItemDto;



@Service
@EnableCaching
public class StockSetItemDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (StockSetItem ss) {
		 dao.saveOrUpdate(ss);
		 
	}
	
	

	public StockSetItem get(PK id) {
		
		return dao.get(StockSetItem.class, id);
	}
	

	@SuppressWarnings("unchecked")
	@Cacheable("producttype")
	public List<StockSetItemDto> getNonGoodsTypes(int warehouseId) {
		
		return (List<StockSetItemDto>) dao.querySQLTransform(
				"select *"
				+ "from v_stock_set_items ssi"
				+ "where ssi.warehouseId=:warehouseId", 
				oneParam("warehouseId", warehouseId), StockSetItemDto.class);
	}
	
	

	public void delete(int id) {
		
		dao.execute("delete from StockSetItem where id=:id", oneParam("id", id));
		
	}



	@SuppressWarnings("unchecked")
	public List<StockSetItem> findBySetId(int stockSetId) {
		
		 List<StockSetItem> list = (List<StockSetItem>) dao.query(
				"from StockSetItem where stockSetId=:stockSetId ", 
				oneParam("stockSetId", stockSetId));
		 
		 return list;
	}


	@SuppressWarnings("unchecked")
	public StockSetItem findEmpty(int stockSetId) {
		
		List<StockSetItem> list = (List<StockSetItem>) dao.query(
				"from StockSetItem where stockSetId=:stockSetId and inStockId=null", 
				oneParam("stockSetId", stockSetId));
		
		return firstListElement(list);
	}
	
	

	
	
	

}
