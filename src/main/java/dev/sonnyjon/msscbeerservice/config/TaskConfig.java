package dev.sonnyjon.msscbeerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Sonny on 9/9/2022.
 */
@EnableAsync
@EnableScheduling
@Configuration
public class TaskConfig
{
    @Bean
    TaskExecutor taskExecutor()
    {
        return new SimpleAsyncTaskExecutor();
    }
}