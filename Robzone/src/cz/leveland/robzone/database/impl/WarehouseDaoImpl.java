package cz.leveland.robzone.database.impl;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Warehouse;



@Service
@EnableCaching
public class WarehouseDaoImpl extends DaoImpl {
	
	
	

	public void saveOrUpdate (Warehouse warehouse) {
		 dao.saveOrUpdate(warehouse);
		 
	}
	
	public Warehouse get(int id) {
		
		return dao.get(Warehouse.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Warehouse> getByCompany(int companyId) {
		
		
		return (List<Warehouse>) dao.query(
				"from Warehouse where companyId=:companyId", 
				oneParam("companyId", companyId));
	}


}
