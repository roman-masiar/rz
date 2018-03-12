package cz.leveland.robzone.transport;

import java.util.HashMap;
import java.util.Map;

import cz.leveland.robzone.database.entity.ProductType;

public class PPLPackNumberGenerator implements PackNumberGenerator{

	public static final int PACK_TYPE_PRIVATE = 1;
	public static final int PACK_TYPE_PRIVATE_COD = 2;
	public static final int PACK_TYPE_BUSINESS = 3;
	public static final int PACK_TYPE_BUSINESS_COD = 4;
	public static final int PACK_TYPE_EXPORT = 5;
	public static final int PACK_TYPE_EXPORT_COD = 6;
	
	Map<Integer,PackNumberSequence> sequences = new HashMap<Integer,PackNumberSequence>();
	
	
	
	public PPLPackNumberGenerator() {
		sequences.put(PACK_TYPE_PRIVATE, new PackNumberSequence(40153369603L, 40153371602L));
		sequences.put(PACK_TYPE_PRIVATE_COD, new PackNumberSequence(40193418814L, 40193420813L));
		sequences.put(PACK_TYPE_BUSINESS, new PackNumberSequence(80154526736l, 80154528735L));
		sequences.put(PACK_TYPE_BUSINESS_COD, new PackNumberSequence(80191057412L, 80191059411L));
		sequences.put(PACK_TYPE_EXPORT, new PackNumberSequence(20151005468L, 20151007467L));
		sequences.put(PACK_TYPE_EXPORT_COD, new PackNumberSequence(20190733247L, 20190735246L));
	}

	@Override
	public String generateNext(String lastNumber, int packType) {
			
		long last = Long.parseLong(lastNumber);
		return (last+1) + "";
	}

	@Override
	public String getFirst(int packType) {
		
		return sequences.get(packType).getStart() + "";
	}

	@Override
	public int getPackType(DeliveryRequest one) {
		
		boolean isExport = one.getTransporter().getCountryId() != one.getCustomer().getCountryId();
		boolean isCompany = one.getCustomer().getRegNo() != null;
		boolean isCod = one.getOrder().getPaymentTypeId() == ProductType.TYPE_PAYMENT_COD;
		
		if (isExport) {
			if (isCod)
				return PACK_TYPE_EXPORT_COD;
			else
				return PACK_TYPE_EXPORT;
		} else {
			if (isCompany) {
				if (isCod)
					return PACK_TYPE_BUSINESS_COD;
				else
					return PACK_TYPE_BUSINESS;
			} else {
				if (isCod)
					return PACK_TYPE_PRIVATE_COD;
				else
					return PACK_TYPE_PRIVATE;
			}
					
		}		
	}	
	


	
	

}
