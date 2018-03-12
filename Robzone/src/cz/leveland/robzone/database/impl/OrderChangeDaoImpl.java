package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.OrderChange;



@Service
@EnableCaching
public class OrderChangeDaoImpl extends DaoImpl {
	
	
	

	public void saveOrUpdate (OrderChange claim) {
		 dao.saveOrUpdate(claim);
		 
	}
	
	public OrderChange get(int id) {
		
		return dao.get(OrderChange.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<OrderChange> getByOrderId(int orderId) {
	
		return (List<OrderChange>) dao.query(
				"from OrderChange where orderId=:orderId", 
				oneParam("orderId", orderId));
	}
	

	
	public void deleteByOrderItemId(int orderItemId) {
		
		dao.execute(
				"delete from OrderChange where orderItemId=:orderItemId", 
				oneParam("orderItemId", orderItemId));
	}
	
	

	
	
	

}
