package cz.leveland.robzone.database.impl;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Invoice;
import cz.leveland.robzone.database.entity.dto.InvoiceDto;



@Service
@EnableCaching
public class InvoiceDaoImpl extends DaoImpl {
	
	private static final String QUERY_STR = 
			"select id as id, type as type, subType as subType, invoiceNo as invoiceNo, partnerId as partnerId, orderId as orderId, orderNo as orderNo, name as partnerName, amount as amount, vat as vat, totalPrice as totalPrice, "
					+ "created as created, duedate as dueDate, taxDate as TaxDate, currencyid as currencyId, invoiceId as invoiceId, currency as currency, "
					+ "regno as regno, taxno as taxno, street as street, city as city ";

	public void saveOrUpdate (Invoice invoice) {
		 dao.saveOrUpdate(invoice);
		 
	}

	public Invoice get(int id) {
		
		return dao.get(Invoice.class, id);
	}
	

	

	public void delete(int id) {
		
		dao.execute("delete from Invoice where id=:id", oneParam("id", id));
		
	}

	@SuppressWarnings("unchecked")
	public List<Invoice> getByOrderId(int orderId) {
	
		return (List<Invoice>) dao.query("from Invoice where orderId=:orderId", oneParam("orderId", orderId));
	}
	
	public List<InvoiceDto> getAll(int companyId) {
		
		@SuppressWarnings("unchecked")
		List<InvoiceDto> list = (List<InvoiceDto>) dao.querySQLTransform(
				QUERY_STR
				+ "from v_invoices "
				+ "WHERE companyId=:companyId ",
				oneParam("companyId", companyId), InvoiceDto.class);
		return list;
	}

	public Invoice findByInvoiceNo(String documentNo) {
		
		return findByColumn("Invoice", "invoiceNo", documentNo);
	}

	public Invoice getLast(int countryId, int subType) {
		
		Map<String,Object> params = twoParams("type", Invoice.TYPE_CUSTOMER, "subType", subType);
		params.put("countryId", countryId);
		
		@SuppressWarnings("unchecked")
		List<Invoice> list = (List<Invoice>) dao.queryMaxResults(
				"select inv from Invoice inv, Partner part where inv.partnerId=part.id and part.countryId=:countryId and inv.type=:type and inv.subType=:subType order by inv.id desc",
				params, 1);
		return firstListElement(list);
	}
	
	

	
	

}
