package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.OutStock;
import cz.leveland.robzone.database.entity.dto.OutStockDto;



@Service
@EnableCaching
public class OutstockDaoImpl extends DaoImpl {
	

	private static final String QUERY_OUTSTOCKS = "select outs.id as id, outs.warehouseid as warehouseId, outs.created as created, " 
			+ "outs.orderitemid as orderItemId, outs.qty as qty, outs.userid as userId, outs.pickerid as pickerId, outs.instockid as inStockId, " 
			+ "outs.productId as productId, outs.setproduct as setProduct, outs.stockItemName as stockItemName, outs.stockItemId as stockItemId, outs.packed as packed, "
			+ "outs.orderno as orderNo, outs.orderId as orderId, outs.orderWarehouseId, "
			+ "outs.customerName as customerName, outs.items as items, outs.hasserialno as hasSerialNo, "
			+ "outs.transportId as transportId, outs.transportName as transportName, outs.providerid as providerId, "
			+ "outs.packageCount as packageCount ";

	public void saveOrUpdate (OutStock payment) {
		 dao.saveOrUpdate(payment);
		 
	}
	
	
	public OutStock get(int id) {
		
		return dao.get(OutStock.class, id);
	}

	/**
	 * goods to be picked and delivered
	 * getPacked returns the first part - items ready to be shipped immediately ("prepacked" items with qty=1)
	 * @param warehouseId
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public List<OutStockDto> getPacked(int warehouseId) {
		
		return (List<OutStockDto>) dao.querySQLTransform(
				QUERY_OUTSTOCKS
				+ "from v_outstocks_packed outs " 				
				+ "where outs.warehouseid=:warehouseId "
				+ "order by productId", 
				oneParam("warehouseId", warehouseId), OutStockDto.class);
	}
	
	
	/**
	 * goods to be picked and delivered
	 * getUnacked returns the second part - items which need to be packed into new box (packed=false  or qty > 1)
	 * @param warehouseId
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public List<OutStockDto> getUnpacked(int warehouseId) {
		
		return (List<OutStockDto>) dao.querySQLTransform(
				QUERY_OUTSTOCKS
				+ "from v_outstocks_unpacked outs " 				
				+ "where outs.warehouseid=:warehouseId "
				+ "order by orderNo", 
				oneParam("warehouseId", warehouseId), OutStockDto.class);
	}
	
	

	public void delete(int orderId) {
		
		dao.execute("delete from OutStock where id=:id", oneParam("id", orderId));
		
	}


	@SuppressWarnings("unchecked")
	public List<OutStock> getSiblings(int orderItemId) {
		
		return (List<OutStock>) dao.query("from OutStock where orderItemId=:orderItemId", oneParam("orderItemId", orderItemId));
	}

	@SuppressWarnings("unchecked")
	public List<OutStock> getAllByDelivery(int deliveryItemId) {
		
		return (List<OutStock>) dao.query("from OutStock where deliveryItemId=:deliveryItemId", oneParam("deliveryItemId", deliveryItemId));
		
	}
	

	@SuppressWarnings("unchecked")
	public  OutStock getByDelivery(int deliveryItemId) {
		
		return findById("OutStock", "deliveryItemId", deliveryItemId);
		
	}
	
	

	
	
	

}
