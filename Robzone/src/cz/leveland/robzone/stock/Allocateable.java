package cz.leveland.robzone.stock;

public interface Allocateable {

	public Integer getId();
	public int getWhatId();
	public boolean isAllocated();
	public void setAllocated(int qty);
}

