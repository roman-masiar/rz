package cz.leveland.robzone.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
 
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"cz.leveland.robzone"})

public class ControlConfiguration extends WebMvcConfigurerAdapter {
 
	
	/*
    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver resolver() {
        return new StandardServletMultipartResolver();
    }
    */
    
    /*
    @Override
	public void addCorsMappings(CorsRegistry registry) {
	registry.addMapping("/**");
	}
	*/
 
   
    
 
}