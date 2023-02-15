package it.pagopa.interop.probing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import it.pagopa.interop.probing.listener.JobFailureHandlingListener;

@Configuration
public class SchedulingConfig {

    private final JobFailureHandlingListener jobFailureHandlingListener;

    public SchedulingConfig(JobFailureHandlingListener jobFailureHandlingListener) {
        this.jobFailureHandlingListener = jobFailureHandlingListener;
    }

    @Bean
    public SchedulerFactoryBean scheduler() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setGlobalJobListeners(jobFailureHandlingListener);
        return schedulerFactory;
    }

}
