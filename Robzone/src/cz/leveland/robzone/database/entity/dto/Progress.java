package cz.leveland.robzone.database.entity.dto;

public class Progress {
	int target;
	int done;
	int good;
	int bad;
	
	
	public Progress() {
	}
	
	public Progress(int target, int done, int good, int bad) {
		super();
		this.target = target;
		this.done = done;
		this.good = good;
		this.bad = bad;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public int getDone() {
		return done;
	}
	public void setDone(int done) {
		this.done = done;
	}
	public int getGood() {
		return good;
	}
	public void setGood(int good) {
		this.good = good;
	}
	public int getBad() {
		return bad;
	}
	public void setBad(int bad) {
		this.bad = bad;
	}
	
	
	
	
}