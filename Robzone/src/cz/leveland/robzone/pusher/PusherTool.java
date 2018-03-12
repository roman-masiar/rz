package cz.leveland.robzone.pusher;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.pusher.rest.Pusher;
import com.pusher.rest.data.Result;
import com.pusher.rest.data.Result.Status;


public class PusherTool {
	
	Log logger = LogFactory.getLog(PusherTool.class);

	private Pusher pusher;

		public PusherTool(String appId, String appKey, String appSecret, String cluster) {
			pusher = new Pusher(appId, appKey, appSecret);
			pusher.setCluster(cluster);
			pusher.setEncrypted(true);
		}

		public Pusher getPusher() {
			return pusher;
		}

		public void setPusher(Pusher pusher) {
			this.pusher = pusher;
		}

		public void trigger(String channel, String event, Object object) {
			
			Result result = pusher.trigger(channel, event, object);
			Status status = result.getStatus();
			logger.debug("Push status for "+channel+"/"+event + " :"+status.toString());
		}
		
		


}
