package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.entity.PK;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Payment;
import cz.leveland.robzone.database.entity.dto.PaymentDto;



@Service
@EnableCaching
public class PaymentDaoImpl extends DaoImpl {
	
	
	

	private static final String QUERY_PAYMENTS = 
			"select pay.id as id, pay.refNo as refNo, pay.created as created, pay.amount as amount, pay.payerid as payerId, "
			+ "custorder.orderno as orderNo "
			+ "from payment pay "
			+ "LEFT JOIN custorder on pay.refno=custorder.orderno ";
	private static final String ORDER_PAYMENTS = "order by pay.created desc";


	public void saveOrUpdate (Payment payment) {
		 dao.saveOrUpdate(payment);
		 
	}
	
	
	public Payment get(PK id) {
		
		return dao.get(Payment.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<PaymentDto> getAll() {
		return (List<PaymentDto>) dao.querySQLTransform(
				QUERY_PAYMENTS
				+ ORDER_PAYMENTS, 
				null, PaymentDto.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentDto> getByOrderNo(String orderNo) {
		return (List<PaymentDto>) dao.querySQLTransform(
				QUERY_PAYMENTS
				+ "where orderNo=:orderNo "
				+ ORDER_PAYMENTS, 
				oneParam("orderNo", orderNo), PaymentDto.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentDto> getByOrderId(int orderId) {
		return (List<PaymentDto>) dao.querySQLTransform(
				QUERY_PAYMENTS
				+ "where orderId=:orderId "
				+ ORDER_PAYMENTS, 
				oneParam("orderId", orderId), PaymentDto.class);
	}
	

	public void delete(int orderId) {
		
		dao.execute("delete from Payment where id=:id", oneParam("id", orderId));
		
	}


	public void flush() {
		dao.flushAndClear();
		
	}



	
	
	

}
