package cz.leveland.robzone.pusher;

import cz.leveland.appbase.util.ResponseData;


public class PushManager {

	PusherTool pusherTool;
	
	public static final String PUSHER_CHANNEL_LIVE_DATA = "live_data";	
	public static final String PUSHER_CHANNEL_APP = "robzone";	
	public static final String PUSHER_EVENT_PROGRESS = "progress";
	private static final String PUSHER_EVENT_STOCKSET_PROGRESS = "stockset-progress";
	private static final String PUSHER_EVENT_DELIVERY_PROGRESS = "delivery-progress";
	public static final String PUSHER_EVENT_INVOICE_PROGRESS = "invoice-progress";
	public static final String PUSHER_EVENT_ORDER = "order";

	
	
	public PushManager(PusherTool pusherTool) {
		this.pusherTool = pusherTool;
	}


	public void trigger(String channel, String event, Object data) {
		pusherTool.trigger(channel, event, data);
	}




	public void reportProgress(Object progress, int companyId, String event) {
		
		ResponseData result = prepData("companyId", companyId);
		result.put("progress", progress);
		trigger(PUSHER_CHANNEL_LIVE_DATA, event, 
				result);
		
	}
	
	public void reportProgress(Object progress, int companyId) {
		
		ResponseData result = prepData("companyId", companyId);
		result.put("progress", progress);
		trigger(PUSHER_CHANNEL_LIVE_DATA, PUSHER_EVENT_PROGRESS, 
				result);
		
	}
	
	public void reportStockSetProgress(Object progress, int companyId) {
		
		ResponseData result = prepData("companyId", companyId);
		result.put("progress", progress);
		trigger(PUSHER_CHANNEL_LIVE_DATA, PUSHER_EVENT_STOCKSET_PROGRESS, 
				result);
		
	}
	
	public void reportDeliveryProgress(Object progress, int companyId) {
		
		ResponseData result = prepData("companyId", companyId);
		result.put("progress", progress);
		trigger(PUSHER_CHANNEL_LIVE_DATA, PUSHER_EVENT_DELIVERY_PROGRESS, 
				result);
		
	}
	
	
	



	private ResponseData prepData(String dataName, Object data) {
		ResponseData pData = new ResponseData();
		pData.put(dataName, data);
		return pData;
	}



	public void reset() {
		
	}


	
}
