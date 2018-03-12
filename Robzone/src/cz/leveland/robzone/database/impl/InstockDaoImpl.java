package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.InStock;
import cz.leveland.robzone.database.entity.dto.InStockDto;
import cz.leveland.robzone.database.entity.dto.InStockProductDto;
import cz.leveland.robzone.database.entity.dto.StockSummaryDto;
import cz.leveland.robzone.stock.StockResourceDto;



@Service
@EnableCaching
public class InstockDaoImpl extends DaoImpl {
	
	private static final String QUERY_INSTOCK = 
			"select inst.id as id, inst.warehouseid as warehouseId, inst.stockitemid as stockItemId, inst.created as created, "
						+ "inst.qty as qty, inst.price as price, inst.pickerid as pickerId, inst.serialno as serialNo, inst.info as info, inst.deliveryitemid as deliveryItemId, "
						+ "stockitem.name as stockItemName "
						+ "from instock inst "
						+ "LEFT JOIN stockitem on inst.stockitemid=stockitem.id ";

	public void flushAndCLear() {
		dao.flushAndClear();
	}

	public void saveOrUpdate (InStock payment) {
		 dao.saveOrUpdate(payment);
		 
	}
	
	
	public InStock get(int id) {
		
		return dao.get(InStock.class, id);
	}

	public InStockDto getDto(int id) {
		
		@SuppressWarnings("unchecked")
		List<InStockDto> list = (List<InStockDto>) dao.queryTransform(
				"select inst.id as id, inst.serialNo as serialNo, si.id as stockItemId, si.name as stockItemName "
				+ "from InStock inst, StockItem si where inst.stockItemId=si.id "
				+ "and inst.id=:id", oneParam("id", id), InStockDto.class);
		return firstListElement(list);
	}
	
	
	/**
	 * checks if instock is available for allocation, ment for those with serialno
	 * @param instockId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkAvailabilitySerial(int inStockId) {
		
		List<InStockDto> list = (List<InStockDto>) dao.querySQLTransform(
				"select fs.id "
				+ "from v_free_serial fs where id=:instockId", 
				oneParam("instockId", inStockId), InStockDto.class);
		
		return !list.isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkAvailabilityNonSerial(int inStockId) {
		
		List<InStockDto> list = (List<InStockDto>) dao.querySQLTransform(
				"select fs.id "
						+ "from v_free_nonserial fs where id=:instockId", 
						oneParam("instockId", inStockId), InStockDto.class);
		
		return !list.isEmpty();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<InStockDto> getByWarehouseId(int warehouseId) {
		
		return (List<InStockDto>) dao.querySQLTransform(
					QUERY_INSTOCK
						+ "where inst.warehouseId=:warehouseId", 
						oneParam("warehouseId", warehouseId), InStockDto.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<InStockDto> getByWarehouseId(int warehouseId, int stockItemId) {
		
		return (List<InStockDto>) dao.querySQLTransform(
				QUERY_INSTOCK
				+ "where inst.warehouseId=:warehouseId and stockItemId=:stockItemId", 
				twoParams("warehouseId", warehouseId, "stockItemId", stockItemId), InStockDto.class);
	}
	
	

	public void delete(int id) {
		
		dao.execute("delete from InStock where id=:id", oneParam("id", id));
		
	}


	public List<StockResourceDto> getFreeSerial(int warehouseId) {
		
		@SuppressWarnings("unchecked")
		List<StockResourceDto> list = (List<StockResourceDto>) dao.querySQLTransform(
				"select id as id, stockitemid as stockItemId, 1 as qty, 0 as pickedQty " 
				+ "from v_free_serial "
				+ "where warehouseId=:warehouseId "
				+ "order by id", 
				oneParam("warehouseId", warehouseId), StockResourceDto.class);
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<StockResourceDto> getFreeSerialSum(int warehouseId) {
		
		return (List<StockResourceDto>) dao.querySQLTransform(
				"select stockitemid as stockItemId, qty as qty, pickedQty as pickedQty "
						+ "from v_free_serial_sum "
						+ "where warehouseId=:warehouseId "
						+ "order by stockitemid", 
						oneParam("warehouseId", warehouseId), StockResourceDto.class);
	}
	
	/**
	 * gets summary of available instocks by stockItemId at particular warehouse
	 * @param warehouseId
	 * @return
	 */
	public List<StockSummaryDto> getStock(int warehouseId) {
		
		@SuppressWarnings("unchecked")
		List<StockSummaryDto> list = (List<StockSummaryDto>) dao.querySQLTransform(
				"select stockItemId as id, name as name, sum as sum "
						+ "from v_stock_summary where warehouseId=:warehouseId "
						+ "order by stockItemId", 
						oneParam("warehouseId", warehouseId), StockSummaryDto.class);
		
		return list;
	}
	


	/**
	 * gets availability of NON SERIALNO products
	 * @param warehouseId
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public List<StockResourceDto> getFreeNonSerial(int warehouseId) {
		
		return (List<StockResourceDto>) dao.querySQLTransform(
				"select id as id, stockitemid as stockItemId, qty as qty, pickedQty as pickedQty "
				+ "from v_free_nonserial "
				+ "where warehouseId=:warehouseId "
				+ "order by stockitemid", 
				oneParam("warehouseId", warehouseId), StockResourceDto.class);
	}

	@SuppressWarnings("unchecked")
	public List<StockResourceDto> getFreeNonSerialSum(int warehouseId) {
		
		return (List<StockResourceDto>) dao.querySQLTransform(
				"select stockitemid as stockItemId, qty as qty, pickedQty as pickedQty "
						+ "from v_free_nonserial_sum "
						+ "where warehouseId=:warehouseId "
						+ "order by stockitemid", 
						oneParam("warehouseId", warehouseId), StockResourceDto.class);
	}
	
	@SuppressWarnings("unchecked")
	public InStock findFreeBySerialNo(String serialNo, int warehouseId) {
		
		//return (InStock)findByColumn("InStock", "serialNo", serialNo);
		
		List<InStockDto> list = (List<InStockDto>) dao.querySQLTransform(
				"select id as id, stockItemId as stockItemId, warehouseId as warehouseId "
				+ "from v_free_serial where serialNo=:serialNo and warehouseId=:warehouseId", 
				twoParams("serialNo", serialNo, "warehouseId", warehouseId), InStockDto.class);
		if (list.isEmpty())
			return null;
		
		InStockDto instockDto = list.get(0);
		return new InStock(instockDto.getId(), instockDto.getStockItemId(), instockDto.getWarehouseId());
				
	}
	
	
	@SuppressWarnings("unchecked")
	public InStockProductDto findProduct(int countryId, int instockId) {
		
		List<InStockProductDto> list = (List<InStockProductDto>) dao.querySQLTransform(
				"select instockId as instockId, productId as productId "
						+ "from v_instock_product where countryId=:countryId and instockId=:instockId", 
						twoParams("countryId", countryId, "instockId", instockId), InStockProductDto.class);
		return firstListElement(list);
	}
	
	
	

}
