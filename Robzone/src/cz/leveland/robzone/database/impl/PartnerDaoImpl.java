package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.entity.dto.PartnerDto;



@Service
@EnableCaching
public class PartnerDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (Partner payment) {
		 dao.saveOrUpdate(payment);
		 
	}
	
	
	public Partner get(int id) {
		
		return dao.get(Partner.class, id);
	}


	@SuppressWarnings("unchecked")
	public List<PartnerDto> getAll() {
		return (List<PartnerDto>) dao.querySQLTransform(
				"select par.id as id, par.name as name, par.familyname as familyName, par.regno as regNo, par.countryid as countryId, "
				+ "par.city as city, par.street as street, par.phone as phone, par.email as email, country.name as countryName "
				+ "from partner par "
				+ "LEFT JOIN country on par.countryid=country.id", 
				null, PartnerDto.class);
	}
	
	

	public void delete(int orderId) {
		
		dao.execute("delete from Partner where id=:id", oneParam("id", orderId));
		
	}



	
	
	

}
