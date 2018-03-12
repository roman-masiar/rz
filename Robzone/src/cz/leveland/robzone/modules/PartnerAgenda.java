package cz.leveland.robzone.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.util.ResponseObject;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.impl.PartnerDaoImpl;

@Service
public class PartnerAgenda extends Agenda{

	
	private static final String AGENDA_NAME = "partner";
	@Autowired
	PartnerDaoImpl partnerDao;
	
	/**
	 * will check if partner exists or add new one
	 * @param partner
	 * @throws WrongInputException 
	 */
	public Partner addPartner(Partner partner) throws WrongInputException {
		try {
			partnerDao.saveOrUpdate(partner);
		} catch (Exception e) {
			e.printStackTrace();
			logError(AGENDA_NAME, "addPartner", "partner couldn't be saved, " + partner.toString());
			throw new WrongInputException();
		}
		return partner;
	}

	public ResponseObject getPartners() {
		return buildResponse("partners", partnerDao.getAll());
		
	}
	
	public ResponseObject getCustomers() {
		return buildResponse("customers", partnerDao.getAll());
		
	}
	
	
}
