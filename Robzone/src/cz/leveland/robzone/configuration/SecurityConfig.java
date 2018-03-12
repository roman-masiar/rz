package cz.leveland.robzone.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import cz.leveland.appbase.cors.ApiCORSFilter;
import cz.leveland.appbase.cors.AuthenticationTokenFilter;
import cz.leveland.appbase.cors.CorsSupportEntryPoint;
import cz.leveland.appbase.usermanag.UserDetailsServiceImpl;



@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = "cz.leveland.itsme")

public class SecurityConfig extends
   WebSecurityConfigurerAdapter {

	private static final Log logger = LogFactory.getLog(SecurityConfig.class);
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	EhCacheConfig ehCachConfig;
	
	@Autowired
	ApiCORSFilter apiCorsFilter;
	
	@Value("${PROFILE}")
	private String activeProfile;
	

	@Bean
	DaoAuthenticationProvider authProvider () {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setUserCache(ehCachConfig.userCache());
		return authProvider;
	}
	
	@Bean
	AuthenticationTokenFilter digestFilter() {
		AuthenticationTokenFilter digestFilter = new AuthenticationTokenFilter();
		//digestFilter.setUserDetailsService(userDetailsService);
		//digestFilter.setAuthenticationEntryPoint(digestEntryPoint());
		//digestFilter.setPasswordAlreadyEncoded(true);
		//digestFilter.setUserCache(ehCachConfig.userCache());
		return digestFilter;
	}
	
	
	
	@Bean
	CorsSupportEntryPoint corsSupportEntryPoint() {
		return new CorsSupportEntryPoint();
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) {
		authManagerBuilder.authenticationProvider(authProvider());
		authManagerBuilder.eraseCredentials(false);
		
	}
	
	
	
  
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
	    http
	      .authorizeRequests()
	        .antMatchers("/sec/**").hasAnyRole("USER")
	        .antMatchers("/admin/**").hasRole("ADMIN")
	      .and()
	      	.addFilterBefore(apiCorsFilter,
	            UsernamePasswordAuthenticationFilter.class)
	      	.addFilterAfter(digestFilter(),
	            UsernamePasswordAuthenticationFilter.class)
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	         .and()
	            .exceptionHandling()
	                    .authenticationEntryPoint(corsSupportEntryPoint())
	      ;
	    http.csrf().disable();
	 // get active profile
	    
	  activeProfile = System.getProperty("PROFILE");
	  logger.info("Security: activeProfile=" + activeProfile);
	    
	  if (activeProfile == null)
	    	http.requiresChannel().antMatchers("/sec/**").requiresSecure();
	 }
  
}