package cz.leveland.robzone.transport;

public interface PackNumberGenerator {

	public String getFirst(int packType);
	public String generateNext(String lastNumber, int packType);
	public int getPackType(DeliveryRequest one);
}
