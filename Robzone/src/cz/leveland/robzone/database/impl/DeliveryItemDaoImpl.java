package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.DeliveryItem;
import cz.leveland.robzone.database.entity.dto.DeliveryDto;
import cz.leveland.robzone.database.entity.dto.InStockDto;



@Service
@EnableCaching
public class DeliveryItemDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (DeliveryItem deliveryItem) {
		 dao.saveOrUpdate(deliveryItem);		 
	}
	

	public DeliveryItem get(int id) {
		
		return dao.get(DeliveryItem.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<DeliveryItem> getByDeliveryId(int deliveryId) {
		return (List<DeliveryItem>) dao.query("from DeliveryItem where deliveryId=:deliveryId", 
				oneParam("deliveryId", deliveryId));
	}
	
	

	public void delete(int id) {
		
		dao.execute("delete from DeliveryItem where id=:id", oneParam("id", id));
		
	}


	/*
	@SuppressWarnings("unchecked")
	public InStockDto getDeliveryStockItems(int deliveryItemId) {
		
		List<InStockDto> list = (List<InStockDto>) dao.querySQLTransform(
				"select name as stockItemName, serialno as serialNo, deliveryItemId as deliveryItemId "
						+ "from v_delivery_stockitems "
						+ "WHERE deliveryItemId=:deliveryItemId ",
						oneParam("deliveryItemId", deliveryItemId), InStockDto.class);
		return firstListElement(list);
	}
	*/
	
	
	

}
