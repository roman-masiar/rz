package cz.leveland.robzone.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.leveland.robzone.database.entity.Invoice;
import cz.leveland.robzone.database.impl.InvoiceDaoImpl;

@Service
public class DocumentNumberGenerator {

	private static final String FIRST_INVOICE = "11000000";
	private static final String FIRST_CREDITNOTE = "21000000";
	
	@Autowired
	InvoiceDaoImpl invoiceDao;
	
	
	public String getInvoiceNo(int countryId) {
		
		Invoice last = invoiceDao.getLast(countryId, Invoice.SUBTYPE_INVOICE);
		if (last == null)
			return FIRST_INVOICE;
		return getNext(last.getInvoiceNo());		
	}

	public String getCreditNoteNo(int countryId) {
		
		Invoice last = invoiceDao.getLast(countryId, Invoice.SUBTYPE_CREDITNOTE);
		if (last == null)
			return FIRST_CREDITNOTE;
		return getNext(last.getInvoiceNo());		
	}
	
	private String getNext(String numberStr) {
		
		Long number = Long.parseLong(numberStr);
		number++;
		return (number+1) + "";
	}
}
