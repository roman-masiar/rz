package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.entity.PK;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Reason;



@Service
@EnableCaching
public class ReasonDaoImpl extends DaoImpl {
	

	public void saveOrUpdate (Reason reason) {
		 dao.saveOrUpdate(reason);		 
	}
	
	public Reason get(PK id) {
		
		return dao.get(Reason.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<Reason> getAll(int companyId) {
		return (List<Reason>) dao.query("from Reason where companyId=:companyId", 
				oneParam("companyId", companyId));
	}
	

	public void delete(int id) {
		
		dao.execute("delete from Reason where id=:id", oneParam("id", id));
		
	}
}
