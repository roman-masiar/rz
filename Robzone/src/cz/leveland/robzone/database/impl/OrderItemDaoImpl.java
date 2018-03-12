package cz.leveland.robzone.database.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.OrderItem;
import cz.leveland.robzone.database.entity.ProductType;
import cz.leveland.robzone.database.entity.dto.OrderItemDto;
import cz.leveland.robzone.exception.WrongDataException;



@Service
@EnableCaching
public class OrderItemDaoImpl extends DaoImpl {
	
	private static final String QUERY_STR = 
			"select oi.id as id, oi.orderid as orderId, oi.qty as qty, oi.unit as unit, oi.currency as currency, oi.unitPrice as unitPrice, oi.price as price,  "
					+ "oi.productprice as productPrice, oi.discountPercent as discountPercent, oi.discount as discount, oi.totalprice as totalPrice, oi.description as description, oi.productId as productId, oi.productName as productName, "
					+ "vatRate as vatRate, vat as vat, oi.invoiceQty as invoiceQty ";


	public void flushAndClear() {
		dao.flushAndClear();
	}
	
	
	public void saveOrUpdate (OrderItem orderItem) {
		 dao.saveOrUpdate(orderItem);
		 
	}
	
	public OrderItem get(int id) {
		return dao.get(OrderItem.class, id); 
	}
		
	
	
	@SuppressWarnings("unchecked")
	public List<OrderItem> getByOrderId(int orderId) {
		
		return (List<OrderItem>) dao.query("from OrderItem where orderId=:orderId", oneParam("orderId", orderId));
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderItem> getNonAllocatedByOrderId(int orderId) {
		
		Map<Integer,Integer> allocated =  getAllocatedByOrderIdDto(orderId);
		List<OrderItem> list = (List<OrderItem>) dao.query("from OrderItem where orderId=:orderId", 
				oneParam("orderId", orderId));
		List<OrderItem> res = new ArrayList<OrderItem>();
		for (OrderItem one:list)
			if (!allocated.containsKey(one.getId()))
				res.add(one);
		return res;
		
	}

	/**
	 * bleeeeee
	 * temp stuff until I learn to do left join HQL 
	 */
	
	@SuppressWarnings("unchecked")
	public Map<Integer,Integer> getAllocatedByOrderIdDto(int orderId) {
		
		/* order items for specific order */		
		
		List<Integer> list = (List<Integer>) dao.querySQL(
				"select oi.id as id "				
				+ "from orderitem oi "
				+ "join outstock outs on oi.id=outs.orderitemid "
				+ "where oi.orderid=:orderId",
				oneParam("orderId", orderId));
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		for (Integer i:list)
			map.put(i, i);
		return map;
	}


	@SuppressWarnings("unchecked")
	public List<OrderItemDto> getByOrderIdDto(int orderId) {
		
		/* order items for specific order */				
		List<OrderItemDto> list = (List<OrderItemDto>) dao.querySQLTransform(
				QUERY_STR				
						+ "from v_order_items oi "
						+ "where oi.orderid=:orderId", 
						oneParam("orderId", orderId), OrderItemDto.class);
		return list;
	}
	
	

	public void delete(int orderId) {
		
		dao.execute("delete from OrderItem where id=:id", oneParam("id", orderId));
		
	}


	@SuppressWarnings("unchecked")
	public void changePaymentType(Integer orderId, int type, int newType) throws WrongDataException {
		
		List<Integer> orderItems = (List<Integer>) dao.query(
					"select oi.id "
					+ "from OrderItem oi, Product prod "
					+ "where oi.productId=prod.id and "
					+ "oi.orderId=:orderId and prod.productTypeId=:prodType", 
					twoParams("orderId", orderId, "prodType", ProductType.TYPE_PAYMENT_CREDIT));
		
		if (orderItems.isEmpty())
			throw new WrongDataException();
		int orderItemId = orderItems.get(0);
		
		dao.executeSql("update orderitem set productid=:productId where id=:orderItemId", 
				twoParams("productId", newType, "orderItemId", orderItemId));
	}


	/**
	 * gets order items for all orders related to list of claimIds
	 * @param claimIds
	 * @return
	 */
	public List<OrderItemDto> getClaimOrderItems(List<Integer> claimIds) {

		String inStr = createFilterList(claimIds);				
		@SuppressWarnings("unchecked")
		List<OrderItemDto> list = (List<OrderItemDto>) dao.querySQLTransform(
				QUERY_STR				
				+ "from v_order_items oi "
				+ "join v_claim_orders co on oi.orderid=co.orderid "
				+ "where co.claimId in " + inStr, 
				null, OrderItemDto.class);
		return list;
	}
	


}
