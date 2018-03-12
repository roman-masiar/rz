package cz.leveland.robzone.database.entity;

import cz.leveland.appbase.database.exceptions.WrongInputException;

public abstract class AbstractPojo {

	public static void notNull(Object... args) throws WrongInputException {
		for (Object one:args) {
			if (one == null)
				throw new WrongInputException();
		}
	}
	public static void notZero(Object... args) throws WrongInputException {
		for (Object one:args) {
			if (one == null)
				throw new WrongInputException();
		}
	}
	
	public boolean dif(String s1, String s2) {
		
		if (s1 == null && s2 != null)
			return true;
		else if (s1 == null && s2 == null)
			return false;
		
		return !s1.equals(s2);
	}
	
	public static String str(String str) {
		if (str == null)
			return "";
		return str;
	}
	
	public int intGetter(Number number) {
		if (number==null)
			return 0;
		return number.intValue();
	}
	
	public double doubleGetter(Number number) {
		if (number==null)
			return 0;
		return number.doubleValue();
	}
}
