package cz.leveland.robzone.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


@Configuration
@EnableScheduling
public class ScheduleConfig {
	
	
	@Bean
	TaskScheduler getScheduler() {
		
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		scheduler.setWaitForTasksToCompleteOnShutdown(false);
		scheduler.setRemoveOnCancelPolicy(true);
		scheduler.setDaemon(true);
		
		//scheduler.setRejectedExecutionHandler(new TaskRejectedExecutionHandler());
		 
		
		return scheduler;
	}
	
	
	
	
	

}
