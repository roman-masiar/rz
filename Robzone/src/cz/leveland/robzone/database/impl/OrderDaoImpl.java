package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.dto.OrderDto;
import cz.leveland.robzone.database.entity.dto.OrderItemDto;



@Service
@EnableCaching
public class OrderDaoImpl extends DaoImpl {
	

	
	private static final String QUERY_ORDERS = 
			"select ord.orderid as id, ord.orderno as orderNo, ord.created as created,  "
				+ "ord.transporttypeid as transportTypeId, ord.transportName as transportName, ord.transProdId as transProdId, ord.paymenttypeid as paymentTypeId, ord.paymentName as paymentName,  ord.payProdId as payProdId, ord.origPaymentType as origPaymentType, ord.status as status, "
				+ "ord.partnerid as partnerId, ord.warehouseId as warehouseId, "
				+ "ord.email as email, ord.name as name, ord.familyName as familyName,  ord.phone as phone, "
				+ "ord.city as city, ord.street as street, ord.zip as zip, ord.countryid as countryId, ord.countrycode as countryCode, "
				+ "ord.currency as currency,  ord.currencyId as currencyId, "
				+ "ord.webPrice as webPrice, ord.webItemPrice as webItemPrice, ord.webItemVat as webItemVat, ord.productPrice as productPrice, "
				+ "ord.paid as paid,  "
				+ "ord.creditAmount as creditAmount, ord.missing as missing, ord.delivered as delivOk, ord.returned as returned, "
				+ "pick.ordered as orderedItems, pick.picked as pickedItems, pick.shipped as shippedItems, pick.delivered as delivered  ";
	
	private static final String FROM_ORDERS = 
			"FROM v_orders ord "
			+ "JOIN v_undelivered on ord.orderid=v_undelivered.id "
			+ "LEFT JOIN v_picked pick on ord.orderid=pick.orderid ";


	

	private static final String ORDER_ORDERS = " ORDER BY custorder.created ";				
				


	public void flushAndClear() {
		dao.flushAndClear();
	}
	


	public void saveOrUpdate (Order order) {
		 dao.saveOrUpdate(order);
		 
	}
	
	public Order get(int orderId) {
		return dao.get(Order.class, orderId);
	}
	
	public Order getByOrderNo(String orderNo) {
		return findByColumn("Order", "orderNo", orderNo);
	}


	
	public Order getLast() {
		@SuppressWarnings("unchecked")
		List<Order> list = (List<Order>) dao.queryMaxResults("from Order o order by id desc", null, 1);
		return firstListElement(list);
	}
	
	@SuppressWarnings("unchecked")
	//@Cacheable(value="order", key="T(java.util.Objects).hash(#orderNo,#companyId)")
	public OrderDto getDto(String orderNo, int companyId) {
		
		
		List<OrderDto> list = (List<OrderDto>) dao.querySQLTransform(
				QUERY_ORDERS + FROM_ORDERS
				+ "WHERE ord.orderno=:orderNo and ord.companyid=:companyId",								 
				twoParams("orderNo", orderNo, "companyId", companyId), OrderDto.class);
		return firstListElement(list);
	}
	
	@SuppressWarnings("unchecked")
	//@Cacheable(value="order", key="T(java.util.Objects).hash(#orderId,#companyId)")
	public OrderDto getDto(int orderId, int companyId) {
		
		List<OrderDto> list = (List<OrderDto>) dao.querySQLTransform(
				QUERY_ORDERS + FROM_ORDERS
				+ "WHERE ord.orderid=:orderId and ord.companyid=:companyId",								 
				twoParams("orderId", orderId, "companyId", companyId), OrderDto.class);
		return firstListElement(list);
	}

	

	@SuppressWarnings("unchecked")
	public List<OrderDto> getPending(int companyId, List<Integer> countryIds) {
		
		/* orders with items who have no outstock record */
		/* objednavky bez vydejek */
		
		String inStr = createFilterList(countryIds);
		return (List<OrderDto>) dao.querySQLTransform(
				QUERY_ORDERS
				+ FROM_ORDERS
				+ "WHERE ord.companyid=:companyId and countryId in " + inStr + " ORDER BY ord.orderno ", 
				oneParam("companyId", companyId), OrderDto.class);
	}
	

	public void delete(int orderId) {
		
		dao.execute("delete from Order where id=:id", oneParam("id", orderId));		
	}

	public void updateInt(int orderId, String fieldName, int val) {
		if (fieldName.contains(";"))
			return;
		dao.execute("update Order o set o." + fieldName + "=:val where o.id=:orderId", twoParams("orderId", orderId, "val", val));
		
	}

	public void updateString(int orderId, String fieldName, String val) {
		if (fieldName.contains(";"))
			return;
		dao.execute("update Order set " + fieldName + "=:val where orderId=:orderId", twoParams("orderId", orderId, "val", val));
		
	}



	
	@SuppressWarnings("unchecked")
	public Integer getPaymentType(int orderId) {
		
		List<Integer> list = (List<Integer>) dao.querySQL(
				"select paymenttypeid as paymentTypeId "
				+ "from v_orders_payment_types "
				+ "where orderid=:orderId", 
				oneParam("orderId", orderId));
		if (list.isEmpty())
			return null;
		int pt = (int)list.get(0);
		return pt;
	}



	public Order getByClaimId(int claimId) {
		
		return findById("Order", "claimId", claimId);
	}




}
