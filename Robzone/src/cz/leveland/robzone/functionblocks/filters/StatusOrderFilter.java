package cz.leveland.robzone.functionblocks.filters;

import java.util.List;

import org.springframework.stereotype.Service;

import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.ProductType;
import cz.leveland.robzone.database.entity.dto.OrderDto;

@Service
public class StatusOrderFilter implements OrderFilter{

	
	
	@Override
	public List<OrderDto> filter(List<OrderDto> orders) {
		//List<OrderDto> result = new ArrayList<OrderDto>();
		//boolean add;
		for (OrderDto one:orders) {
			
			@SuppressWarnings("unused")
			int status = getOrderStatus(one);
			
			/*
			if (status != Order.STATUS_SHIPPED)
				result.add(one);
			*/
				
		}
		return orders;
		
		}

	public int getOrderStatus(OrderDto one) {
		
		
		one.setStatus(Order.STATUS_NEW);
		
		if (one.getTransProdId() == null || one.getPayProdId() == null) {
			one.setStatus(Order.STATUS_PROBLEM);
			return Order.STATUS_PROBLEM;
		}
		
		if (one.isReturned())
			one.setStatus(Order.STATUS_RETURNED);
		else if (one.isDelivOk())
			one.setStatus(Order.STATUS_DELIVERED);
		else if (one.getShippedItems().intValue() > 0)
			one.setStatus(Order.STATUS_SHIPPED);
		else if (one.getOrderedItems()==one.getPickedItems())
			one.setStatus(Order.STATUS_PICKED);
		else if (one.isMissing())
			one.setStatus(Order.STATUS_MISSING);
		else if (
				one.getPaymentTypeId() == ProductType.TYPE_PAYMENT_COD || 
				one.getPaymentTypeId() == ProductType.TYPE_PAYMENT_CREDIT && one.getCreditAmount() != null  || 
				one.getPaymentTypeId() == ProductType.TYPE_PAYMENT_WIRE && one.getPaid() != null && one.getPaid()+0.1 >= one.getWebPrice() || 
				one.getPaymentTypeId() == ProductType.TYPE_PAYMENT_ONLINE && (one.getPaid() != null && one.getPaid()+0.1 >= one.getWebPrice()) 
				) {
			one.setStatus(Order.STATUS_PROCESSED);
			
		} else if (
				one.getPaymentTypeId() == ProductType.TYPE_PAYMENT_CREDIT && one.getCreditAmount()== null || 
				one.getPaymentTypeId() == ProductType.TYPE_PAYMENT_WIRE && (one.getPaid() == null || one.getPaid() < one.getWebPrice()) ||
				one.getPaymentTypeId() == ProductType.TYPE_PAYMENT_ONLINE && (one.getPaid() == null || one.getPaid() < one.getWebPrice())
				) {
			one.setStatus(Order.STATUS_WAITING);
			
		} 
		
		return one.getStatus();
	}
	


}
