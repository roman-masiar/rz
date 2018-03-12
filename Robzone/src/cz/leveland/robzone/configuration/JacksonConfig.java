package cz.leveland.robzone.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class JacksonConfig {
	
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter mappingAdapter = new RequestMappingHandlerAdapter();
		
		HttpMessageConverter<?> messageConverter = new MappingJackson2HttpMessageConverter();
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(messageConverter);
		mappingAdapter.setMessageConverters(list);
		
		String[] supportedHttpMethods = { "POST", "GET", "HEAD" };
        mappingAdapter.setSupportedMethods(supportedHttpMethods);
        
		return mappingAdapter;
	}
	

}

