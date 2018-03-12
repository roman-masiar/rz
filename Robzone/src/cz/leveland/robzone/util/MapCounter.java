package cz.leveland.robzone.util;

import java.util.HashMap;

import cz.leveland.robzone.exception.MapCounterException;
import cz.leveland.robzone.exception.OverAllocationException;

public class MapCounter extends HashMap<Integer,Integer> {
	
	private static final long serialVersionUID = 1L;

	public void up(int id, int qty) {
		
		int val = 0;
		if(!containsKey(id)) 
			put(id, 0);
		else
			val = get(id);
		
		val += qty;
		put(id, val);		
	}

	public int down(int id, int qty) throws MapCounterException, OverAllocationException {
		
		int val = 0;
		if(!containsKey(id)) 
			throw new MapCounterException();
		
		val = get(id);
		if (val < qty)
			throw new OverAllocationException();
		
		val -= qty;
		put(id, val);
		
		return val;
	}

}
