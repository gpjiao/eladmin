package me.zhengjie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class I18nConfig
{
    @Value("${spring.messages.basename}")
    private String basename;
    
    @Value("${spring.messages.cache-seconds}")
    private long cacheMillis;
    
    @Value("${spring.messages.encoding}")
    private String encoding;
    
    @Bean(name = "messageSource")
    public MessageSource getMessageResource()
    {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding(encoding);
        messageSource.setCacheMillis(cacheMillis);
        return messageSource;
    }
}
