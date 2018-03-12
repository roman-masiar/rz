package cz.leveland.robzone.stock;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Allocations extends ArrayList<Allocation> {

	public int getSum() {
		int sum = 0;
		for (Allocation one:this)
			sum += one.qty;
		return sum;
	}
}
