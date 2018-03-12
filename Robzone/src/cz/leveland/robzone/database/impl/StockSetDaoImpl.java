package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.StockSet;
import cz.leveland.robzone.database.entity.dto.StockSetDto;



@Service
@EnableCaching
public class StockSetDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (StockSet ss) {
		 dao.saveOrUpdate(ss);
		 
	}
	
	

	public StockSet get(int id) {
		
		return dao.get(StockSet.class, id);
	}
	

	@SuppressWarnings("unchecked")	
	public List<StockSetDto> getAvailable(int warehouseId) {
		
		return (List<StockSetDto>) dao.querySQLTransform(
				"select avs.productid as productId, avs.cnt as cnt, avs.name as name "
				+ "from v_available_stocksets avs "
				+ "where avs.warehouseId=:warehouseId", 
				oneParam("warehouseId", warehouseId), StockSetDto.class);
	}
	
	
	@SuppressWarnings("unchecked")	
	public List<StockSetDto> getFree(int warehouseId) {
		
		return (List<StockSetDto>) dao.querySQLTransform(
				"select id as id, productid as productId "
						+ "from v_free_stocksets fss "
						+ "where fss.warehouseId=:warehouseId", 
						oneParam("warehouseId", warehouseId), StockSetDto.class);
	}
	
	

	public void delete(int id) {
		
		dao.execute("delete from StockSetItem where id=:id", oneParam("id", id));
		
	}



	
	
	

}
