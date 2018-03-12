package cz.leveland.robzone.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.leveland.robzone.database.entity.dto.ProductSetItemDto;
import cz.leveland.robzone.database.entity.dto.StockSetDto;
import cz.leveland.robzone.database.impl.InstockDaoImpl;
import cz.leveland.robzone.database.impl.OutstockDaoImpl;
import cz.leveland.robzone.database.impl.ProductDaoImpl;
import cz.leveland.robzone.database.impl.ProductSetItemDaoImpl;
import cz.leveland.robzone.database.impl.StockItemDaoImpl;
import cz.leveland.robzone.database.impl.StockSetDaoImpl;
import cz.leveland.robzone.exception.MapCounterException;
import cz.leveland.robzone.exception.OverAllocationException;
import cz.leveland.robzone.exception.StockAllocationException;
import cz.leveland.robzone.exception.StockSetCreationException;
import cz.leveland.robzone.util.MapCounter;

@Service
public class FreeStockAllocator {

	/* safety stock */
	public static final int SAFETY_STOCK_SERIAL = 0;
	private static final int SAFETY_STOCK_NONSERIAL = 0;


	@Autowired
	ProductDaoImpl productDao;

	@Autowired
	StockItemDaoImpl stockItemDao;
	
	@Autowired
	StockSetDaoImpl stockSetDao;
	
	
	@Autowired
	InstockDaoImpl instockDao;

	@Autowired
	OutstockDaoImpl outstockDao;
	
	@Autowired
	ProductSetItemDaoImpl productSetItemDao;
	
	/* stock sets */
	List<StockSetDto> freeStockSets = new ArrayList<StockSetDto>();

	/* serial instocks */
	List<StockResourceDto> freeSerial;
	List<StockResourceDto> freeSerialSum;
	MapCounter freeSerialMap = new MapCounter();
	
	/* non serialno instocks */
	List<StockResourceDto> freeNonSerial;
	List<StockResourceDto> freeNonSerialSum;
	Map<Integer,Integer> freeNonSerialMap = new HashMap<Integer,Integer>();
	
	
	
	
	
	public void init(int warehouseId) {
		freeStockSets = stockSetDao.getFree(warehouseId);
		
		freeSerial = instockDao.getFreeSerial(warehouseId);
		freeSerialSum = instockDao.getFreeSerialSum(warehouseId);
		freeSerialMap = makeFreeMap(freeSerialSum);
		
		freeNonSerial = instockDao.getFreeNonSerial(warehouseId);
		freeNonSerialSum = instockDao.getFreeNonSerialSum(warehouseId);
		freeNonSerialMap = makeFreeMap(freeNonSerialSum);
	}
	
	private MapCounter makeFreeMap(List<StockResourceDto> freeList) {
		MapCounter sum = new MapCounter();
		int stockItemId;
		for (StockResourceDto one:freeList) {
			stockItemId = one.getStockItemId();
			//if (!sum.containsKey(stockItemId))
			//	sum.put(stockItemId, 0);
			//sum.get(stockItemId) + 
			sum.put(stockItemId, one.getAvailable());			
		}
		return sum;
	}
	
	
	
	
	public Integer allocateStockSet(int productId) {
		
		for (int i=0; i<freeStockSets.size(); i++) {
			StockSetDto one = freeStockSets.get(i);
			if (one.isAllocated() || one.getProductId() != productId)
				continue;
			one.setAllocated(1);
			return one.getId();
		}
		return null;
	}
	
	/* not used now since serial numbers are not known until piciking */
	/*
	public Integer allocateInStockSerial(int stockItemId) {
		
		for (int i=0; i<freeSerial.size(); i++) {
			InStockDto one = freeSerial.get(i);
			if (one.isAllocated() || one.getStockItemId() != stockItemId)
				continue;
			one.setAllocated(1);
			return one.getId();
		}
		return null;
	}
	*/
	
	/**
	 * get availability of hasSerialNo items on stock for particular stociItemId
	 * @param stockItemId
	 * @param setQty 
	 * @return
	 * @throws StockAllocationException 
	 */
	public int freeSerialAvailableForOrder(int stockItemId, int setQty) throws StockAllocationException  {
		Integer cnt = freeSerialMap.get(stockItemId);
		if (cnt == null)
			throw new StockAllocationException();
		return cnt - setQty - SAFETY_STOCK_SERIAL;
	}
	
	/** 
	 * checks availability of serial stock item and counts allocation
	 * @param stockItemId
	 * @param setQty
	 * @return
	 * @throws StockAllocationException 
	 * 
	 */
	public int allocateStockSerial(int stockItemId, int setQty) throws StockAllocationException {
		Integer cnt = freeSerialMap.get(stockItemId);
		if (cnt == null)
			throw new StockAllocationException();
		if (cnt >= setQty) {
			try {
				freeSerialMap.down(stockItemId, setQty);
			} catch (MapCounterException | OverAllocationException e) {
				throw new StockAllocationException();
			}
		} else
			return 0;
		return setQty;
	}
	
	/**
	 * availability of non hasSerialNo items on stock for particular stociItemId
	 * @param stockItemId
	 * @return
	 * @throws StockAllocationException
	 */
	public int freeNonSerialAvailableForOrder(int stockItemId, int qty) throws StockAllocationException  {
		Integer sum = freeNonSerialMap.get(stockItemId);
		if (sum == null)
			throw new StockAllocationException();
		return sum - qty - SAFETY_STOCK_NONSERIAL;
	}
	
	/*
	public boolean allocateSerial(int stockItemId) throws OrderProcessingException {
		Integer cnt = freeSerialSum.get(stockItemId);
		if (cnt == null)
			throw new OrderProcessingException();
		if (cnt <= 0)
			return false;
		cnt--;
		freeSerialSum.put(stockItemId, cnt);
		return true;
	}
	*/
	
	public Allocations allocateInStockNonSerial(int neededQty, int whatId) throws OverAllocationException {
		
		Allocations allocations = new Allocations();
		boolean tried = false;
		int toBeAllocated = neededQty, allocateNow = 0;
		
		for (StockResourceDto instockPicked: freeNonSerial) {
			/* find desired stock item id */
			if (instockPicked.getStockItemId() != whatId) {
				if (tried) 
					break; 
				else 
					continue;
			}			
			tried = true;
			int available = instockPicked.getAvailable();
			if (available == 0)
				continue;
			
			/* if found, allocate */
			if (available >= toBeAllocated) {
				allocateNow = toBeAllocated;
				toBeAllocated = 0;
			} else {
				allocateNow = available;
				toBeAllocated -= allocateNow;
			}
			if (allocateNow <= 0)
				break;
			
			/* create allocation*/
			instockPicked.allocate(allocateNow);
			allocations.add(new Allocation(instockPicked.getId(), allocateNow));
					
		}
		return allocations;
	}

	/**
	 * checks if there are enough serial/non serial items for stock sets creation
	 * finds the hasSerialNo item in product set and checks its availability on stock
	 * @param ssItems
	 * @param setQty
	 * @return Map<Integer,Integer> showing missing stockItemids and missing qty, otherwise empty if OK
	 * @throws StockSetCreationException
	 */
	public Map<Integer,Integer> checkFreeAvailability(List<ProductSetItemDto> ssItems, int setQty) throws StockAllocationException {
		
		Map<Integer,Integer> missing = new HashMap<Integer,Integer>();
		int serialOne = 0;
		
		for (ProductSetItemDto psi:ssItems) {
			int stockItemId = psi.getStockItemId();
			
			if (psi.isHasSerialNo()) {
				serialOne = stockItemId;								
			} else {
				int wanted = setQty * psi.getQty();
				int availableQty = freeNonSerialAvailableForOrder(stockItemId, wanted);
				if (availableQty < 0)
					missing.put(stockItemId, -availableQty);
			}
		}
		
		/* if there is a serialNo item */
		if (serialOne > 0) {
			int availableQty = freeSerialAvailableForOrder(serialOne, setQty);
			if (availableQty < 0)
					missing.put(serialOne, -availableQty);
		}
		
			
		return missing;

		
	}


}
