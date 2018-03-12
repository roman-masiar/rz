package cz.leveland.robzone.database.impl;

import java.util.List;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Delivery;
import cz.leveland.robzone.database.entity.dto.DeliveryDto;
import cz.leveland.robzone.database.entity.dto.DeliveryItemDto;
import cz.leveland.robzone.database.entity.dto.OrderDto;



@Service
@EnableCaching
public class DeliveryDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (Delivery delivery) {
		 dao.saveOrUpdate(delivery);
		 
	}

	public Delivery get(int id) {
		
		return dao.get(Delivery.class, id);
	}
	

	

	public void delete(int id) {
		
		dao.execute("delete from Delivery where id=:id", oneParam("id", id));
		
	}

	@SuppressWarnings("unchecked")
	public Delivery getByOrderId(int orderId) {
		
		List<Delivery> list =(List<Delivery>) dao.query("from Delivery where orderId=:orderId", 
				oneParam("orderId", orderId));
		return firstListElement(list);
	}


	@SuppressWarnings("unchecked")
	public List<DeliveryDto> getAll(int warehouseId) {
				
		return (List<DeliveryDto>) dao.querySQLTransform(
				"select deliveryid as id, warehouseId as warehouseId, countryCode as countryCode, name as name, email as email, phone as phone, city as city, orderno as orderNo, created as created, "
				+ "currency as currency, totalPrice as totalPrice, productItemPrice as productItemPrice, "
				+ "transportName as transportName, transProdId as transProdId, packNumber as packNumber, delivered as delivered, "
				+ "returned as returned, status as status, backStock as backStock "
				+ "from v_delivery "
				+ "WHERE warehouseId=:warehouseId "
				+ "ORDER BY countryId, transportName, deliveryId ", 
				oneParam("warehouseId", warehouseId), DeliveryDto.class);
	}
	
	@SuppressWarnings("unchecked")
	public DeliveryDto getDeliveryOrderPartner(int deliveryId) {
		
		List<DeliveryDto> list = (List<DeliveryDto>) dao.querySQLTransform(
				"select deliveryid as id, customerId as customerId, name as name, email as email, phone as phone, city as city, countryId as countryId, "
				+ "orderId as orderId, orderno as orderNo, delivered as delivered "
						+ "from v_delivery_short "
						+ "WHERE deliveryId=:deliveryId ",
						oneParam("deliveryId", deliveryId), DeliveryDto.class);
		return firstListElement(list);
	}

	public Delivery getByPackNo(String packNumber) {

		return findByColumn("Delivery", "number", packNumber);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryItemDto> getOne(int deliveryId) {
				
		return (List<DeliveryItemDto>) dao.querySQLTransform(
				"select deliveryItemId as id, outQty as qty "
				+ "from v_orderitems_chain "
				+ "WHERE deliveryId=:deliveryId", 
				oneParam("deliveryId", deliveryId), DeliveryItemDto.class);
	}

	
	
	

}
