package cz.leveland.robzone.transport;

public class PackNumberSequence {

	long start;
	long end;
	
	public PackNumberSequence(long start, long end) {
		super();
		this.start = start;
		this.end = end;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
	
}
