package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.PkUnit;
import cz.leveland.robzone.database.entity.PkVatRate;
import cz.leveland.robzone.database.entity.Unit;
import cz.leveland.robzone.database.entity.VatRate;



@Service
@EnableCaching
public class UnitDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (Unit unit) {
		 dao.saveOrUpdate(unit);
		 
	}
	
	
	public Unit get(PkUnit id) {
		
		return dao.get(Unit.class, id);
	}

	

	@SuppressWarnings("unchecked")
	public List<VatRate> getAll(int companyId) {
		return (List<VatRate>) dao.query("from Unit where companyId=:companyId", 
				oneParam("companyId", companyId));
	}
	
	

	public void delete(int id) {
		
		dao.execute("delete from VatRate where id=:id", oneParam("id", id));
		
	}



	
	
	

}
