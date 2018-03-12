package cz.leveland.robzone.pusher;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;


public class PusherClientTool {
	
	Channel channel, orderBookChannel;
	
	public PusherClientTool(String appKey, String pushChannel, String pushChannelOrderBook) {
		
		PusherOptions options = new PusherOptions();
		Pusher pusher = new Pusher(appKey, options);
		channel = pusher.subscribe(pushChannel);
		orderBookChannel = pusher.subscribe(pushChannelOrderBook);
		pusher.connect();
	}
	
	public void bind(String event, SubscriptionEventListener listener) {
		channel.bind(event, listener); 
		
	}
	public void bindOrderBook(String event, SubscriptionEventListener listener) {
		orderBookChannel.bind(event, listener); 
		
	}

}
