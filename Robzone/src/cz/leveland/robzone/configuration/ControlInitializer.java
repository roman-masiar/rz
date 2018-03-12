package cz.leveland.robzone.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//@Configuration
public class ControlInitializer extends
        AbstractAnnotationConfigDispatcherServletInitializer {
 
	
	
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { ControlConfiguration.class};
    }
    
 
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }
 
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
        
    }
    
    @Override
    public void onStartup(ServletContext servletContext) 
            throws ServletException {
    	
    	System.out.println("Active (robzone) profile: " + System.getProperty("PROFILE"));
    	
        servletContext
            .addFilter("securityFilter", 
                       new DelegatingFilterProxy("springSecurityFilterChain"))
            .addMappingForUrlPatterns(null, false, "/*");

        super.onStartup(servletContext);
    }
 
    /*
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(getMultipartConfigElement());
    }
    
    
    
    private MultipartConfigElement getMultipartConfigElement() {
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement( LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
        return multipartConfigElement;
    }
    */
 
    /*
    private static final String LOCATION = "tmp/"; // Temporary location where files will be stored
 
    private static final long MAX_FILE_SIZE = 5242880; // 5MB : Max file size.
                                                        // Beyond that size spring will throw exception.
    private static final long MAX_REQUEST_SIZE = 20971520; // 20MB : Total request size containing Multi part.
     
    private static final int FILE_SIZE_THRESHOLD = 0; // Size threshold after which files will be written to disk
    */
}