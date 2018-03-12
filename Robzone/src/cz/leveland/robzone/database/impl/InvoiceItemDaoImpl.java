package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Invoice;
import cz.leveland.robzone.database.entity.InvoiceItem;
import cz.leveland.robzone.database.entity.dto.ForInvoiceDto;
import cz.leveland.robzone.database.entity.dto.InvoicedDto;



@Service
@EnableCaching
public class InvoiceItemDaoImpl extends DaoImpl {
	
	public void flushAndClear() {
		dao.flushAndClear();
	}

	public void saveOrUpdate (InvoiceItem invoiceItem) {
		 dao.saveOrUpdate(invoiceItem);
		 
	}

	public InvoiceItem get(int id) {
		
		return dao.get(InvoiceItem.class, id);
	}
	

	

	public void delete(int id) {
		
		dao.execute("delete from InvoiceItem where id=:id", oneParam("id", id));
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ForInvoiceDto> getForInvoice(int companyId) {
		
		return (List<ForInvoiceDto>) dao.querySQLTransform(
				"select orderId as orderId, orderItemId as orderItemId, productId as productId, name as name, description as description, "
				+ "ordQty as ordQty, unitprice as unitPrice, price as price, discount as discount, "
				+ "ordQty * price as totalPrice, delQty as delQty, "
				+ "productUnitPrice as productUnitPrice, productPrice as productPrice, "
				+ "deliveryItemId as deliveryItemId, vatRate as vatRate, orderVatRate as orderVatRate, outstocks as outstocks, deliveries as deliveries, sumDeliv as sumDeliv, sumUndeliv as sumUndeliv "
				+ "from v_for_invoice where companyId=:companyId", oneParam("companyId", companyId), ForInvoiceDto.class);
	}

	@SuppressWarnings("unchecked")
	public List<InvoiceItem> getByInvoiceId(int invoiceId) {
		
		return (List<InvoiceItem>) dao.query (				
						"from InvoiceItem where invoiceId=:invoiceId", oneParam("invoiceId", invoiceId));
	}
	
	
	@SuppressWarnings("unchecked")
	public List<InvoicedDto> getInvoicedSumByVatRate(int invoiceId) {
		
		return (List<InvoicedDto>) dao.querySQLTransform(
				"select invoiceQty as qty, totalPrice as price, vat as vat, vatRate as vatRate "
				+ "from v_invoiced_sum_vatrate where invoiceId=:invoiceId", 
				oneParam("invoiceId", invoiceId), InvoicedDto.class);
	}

	public InvoiceItem getByDeliveryItemId(Integer did) {
		
		return findById("InvoiceItem", "deliveryItemId", did);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
