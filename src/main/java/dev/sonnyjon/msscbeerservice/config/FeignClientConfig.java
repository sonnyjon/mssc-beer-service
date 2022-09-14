package dev.sonnyjon.msscbeerservice.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Sonny on 9/8/2022.
 */
@Configuration
public class FeignClientConfig
{
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(
            @Value("${sfg.brewery.inventory-user}") String inventoryUser,
            @Value("${sfg.brewery.inventory-password}")String inventoryPassword
    )
    {
        return new BasicAuthRequestInterceptor( inventoryUser, inventoryPassword );
    }
}