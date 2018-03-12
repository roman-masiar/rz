package cz.leveland.robzone.api;

public abstract class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double x = 20;
		for (int i=0;i<100; i++)
			x += 1.0d;
		x-=21.0d;
		
		System.out.println("x " + x);
		if (x == 99.0)
			System.out.println("Ok");
		
		
	}

}
