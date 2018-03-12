package cz.leveland.robzone.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;

@Configuration
@EnableCaching
public class EhCacheConfig {

    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {

        return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
    }


    @Bean  
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {

        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();

        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);

        return cacheManagerFactoryBean;
    }
    
    @Bean
    public EhCacheFactoryBean userCacheBackend () {
    	EhCacheFactoryBean backEnd = new EhCacheFactoryBean();
    	backEnd.setCacheManager(ehCacheCacheManager().getCacheManager());
    	backEnd.setCacheName("userCache");
    	return backEnd;
    	
    }
    
    @Bean
    public EhCacheBasedUserCache userCache() {
    	EhCacheBasedUserCache userCache = new EhCacheBasedUserCache();
    	userCache.setCache(userCacheBackend().getObject());
    	return userCache;
    }
    
    
}