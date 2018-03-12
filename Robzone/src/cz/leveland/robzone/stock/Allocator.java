package cz.leveland.robzone.stock;

import java.util.ArrayList;
import java.util.List;

import cz.leveland.robzone.database.entity.dto.InStockPickedDto;
import cz.leveland.robzone.exception.OverAllocationException;

public class Allocator {

	/**
	 * 	 
	 * used to allocate items with available instocks
	 * produces one or more allocations
	 * 

	 * @param neededQty
	 * @param whatId
	 * @return list of allocations
	 * @throws OverAllocationException
	 */
	public static List<Allocation> allocateAvailableNonSerial(int neededQty, int whatId, List<InStockPickedDto> available) throws OverAllocationException {
		
		List<Allocation> allocations = new ArrayList<Allocation>();
		boolean tried = false;
		int toBeAllocated = neededQty, allocateNow = 0;
		
		for (InStockPickedDto instockPicked: available) {
			/* find desired stock item id */
			if (instockPicked.getStockItemId() != whatId)
				if (tried) break; else continue;
			tried = true;	
			
			/* if found, allocate */
			if (instockPicked.getAvailable() >= toBeAllocated) {
				allocateNow = toBeAllocated;
				toBeAllocated = 0;
			} else {
				allocateNow = instockPicked.getAvailable();
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
	
}
