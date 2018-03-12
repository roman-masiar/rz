package cz.leveland.robzone.database.entity.dto;

public class CountDto {

	int id;
	Number sum;
	Number count;
	
	
	public CountDto() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSum() {
		return sum.intValue();
	}

	public void setSum(Number sum) {
		this.sum = sum;
	}

	public int getCount() {
		return count.intValue();
	}

	public void setCount(Number count) {
		this.count = count;
	}

	
	
}
