package cz.leveland.robzone.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.leveland.robzone.pusher.PushManager;
import cz.leveland.robzone.pusher.PusherTool;







@Configuration
public class PusherConfig {

	@Value("${PUSHER_APP_ID}")
	String appId;
	@Value("${PUSHER_APP_KEY}")
	String appKey;
	@Value("${PUSHER_APP_SECRET}")
	String appSecret;
	@Value("${PUSHER_APP_CLUSTER}")
	String cluster;

	Log logger = LogFactory.getLog(PusherConfig.class);
	
	public PusherTool getPusherTool() {
		logger.info("Constructing pusher tool, id:"+appId+", key:"+appKey+", sec: ****, cluster:"+cluster);
		return new PusherTool(appId, appKey, appSecret, cluster);
	}
	

	@Bean 
	public PushManager getPushManager() {
		return new PushManager(getPusherTool());
	}
}
