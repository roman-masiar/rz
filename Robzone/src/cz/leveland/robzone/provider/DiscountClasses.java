package cz.leveland.robzone.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.leveland.robzone.database.entity.DiscountItem;
import cz.leveland.robzone.database.impl.DiscountItemDaoImpl;

@Service
public class DiscountClasses {

	@Autowired
	DiscountItemDaoImpl discountItemDao;
	
	private Map<Integer,List<DiscountItem>> discounts = new HashMap<Integer,List<DiscountItem>>();
	
	
	public double getDiscount(int classId, int qty) {
	
		List<DiscountItem> classDiscounts;
		if (!discounts.containsKey(classId)) {
			classDiscounts = discountItemDao.getByClassId(classId);
			discounts.put(classId, classDiscounts);
		} else
			classDiscounts = discounts.get(classId);
		
		if (classDiscounts.isEmpty())
			return 0.0;
		
		for (int i=classDiscounts.size() - 1; i>=0; i--) {
			DiscountItem one = classDiscounts.get(i);
			if(one.getQty() < qty)
				return one.getDiscount();
		}
		return 0.0;
	}
	
	
}
