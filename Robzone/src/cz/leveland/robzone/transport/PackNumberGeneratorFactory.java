package cz.leveland.robzone.transport;

import cz.leveland.robzone.database.entity.Partner;

public class PackNumberGeneratorFactory {

	public static PackNumberGenerator getInstance(Partner partner) {
		
		if (partner.getName().equals("PPL"))
			return new PPLPackNumberGenerator();
		else if (partner.getName().equals("Zásilkovna"))
			return new ZasilkovnaPackNumberGenerator();
		else 
			return null;
	}
}
