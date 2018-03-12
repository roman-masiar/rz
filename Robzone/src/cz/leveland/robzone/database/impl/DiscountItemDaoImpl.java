package cz.leveland.robzone.database.impl;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.entity.PK;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Country;
import cz.leveland.robzone.database.entity.DiscountItem;



@Service
@EnableCaching
public class DiscountItemDaoImpl extends DaoImpl {
	
	
	

	public void saveOrUpdate (DiscountItem item) {
		 dao.saveOrUpdate(item);
		 
	}
	
	public DiscountItem get(int id) {
		
		return dao.get(DiscountItem.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DiscountItem> getByClassId(int classId) {
		
		return (List<DiscountItem>) dao.query(
				"from DiscountItem where discountClassId=:classId order by id", 
				oneParam("classId", classId));
	}
	


	
	
	

}
