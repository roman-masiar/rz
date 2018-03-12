package cz.leveland.robzone.database.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import cz.leveland.appbase.database.impl.DaoImpl;
import cz.leveland.robzone.database.entity.Product;
import cz.leveland.robzone.database.entity.StockItem;
import cz.leveland.robzone.database.entity.dto.PickStockItemDto;



@Service
@EnableCaching
public class StockItemDaoImpl extends DaoImpl {
	
	
	

	public void saveOrUpdate (StockItem stockItem) {
		 dao.saveOrUpdate(stockItem);
		 
	}
	
	
	@Cacheable("stockitem")
	public StockItem get(int id) {
		
		return dao.get(StockItem.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<StockItem> getAll(int companyId) {
		return (List<StockItem>) dao.query("from StockItem where companyId=:companyId", 
				oneParam("companyId", companyId));
	}
	

	public void delete(int orderId) {
		
		dao.execute("delete from StockItem where id=:id", oneParam("id", orderId));
		
	}

	
	public StockItem findForOrderItem(int orderItemId) {
		@SuppressWarnings("unchecked")
		List<StockItem> list = (List<StockItem>) dao.query("select si from "
				+ "OrderItem oi, Product prod, ProductSetItem psi, StockItem si "
				+ "where oi.id=:orderItemId and si.hasSerialNo=true "
				+ "and oi.productId=prod.id and prod.id=psi.productId and psi.stockItemId=si.id ", 
				oneParam("orderItemId", orderItemId));
		
		return firstListElement(list);
	}


	@SuppressWarnings("unchecked")
	public List<StockItem> getList(Set<Integer> keySet) {
		
		if (keySet.isEmpty())
			return null;
		String inList = createFilterList(keySet);
		return (List<StockItem>) dao.query("from StockItem where id in " + inList, null);
		
	}

	/*
	 int orderItemId;
	String productName;
	boolean setProduct;
	int productId;
	int orderId;
	Integer si1id;
	String si1name;
	boolean si1sn;
	Integer si2id;
	String si2name;
	boolean si2sn;
	 */

	@SuppressWarnings("unchecked")
	public List<PickStockItemDto> getPickItems(Integer orderId) {
		
		return (List<PickStockItemDto>) dao.querySQLTransform(
				"select orderItemId as orderItemId, setProduct as setProduct, productId as productId, "
				+ "orderId as orderId,  si1id as si1id, si1sn as si1sn, si2id as si2id, si2sn as si2sn "
				+ "from v_pick_stockitems "
				+ "where orderId=:orderId", 
				oneParam("orderId", orderId), 
				PickStockItemDto.class);
	}


	

	
	
	

}
