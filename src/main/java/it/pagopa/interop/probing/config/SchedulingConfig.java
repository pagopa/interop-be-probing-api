/*
 * 
 */
package it.pagopa.interop.probing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import it.pagopa.interop.probing.listener.JobFailureHandlingListener;

/**
 * The Class SchedulingConfig.
 */
@Configuration
public class SchedulingConfig {

	/** The job failure handling listener. */
	private final JobFailureHandlingListener jobFailureHandlingListener;

	/**
	 * Instantiates a new scheduling config.
	 *
	 * @param jobFailureHandlingListener the job failure handling listener
	 */
	public SchedulingConfig(JobFailureHandlingListener jobFailureHandlingListener) {
		this.jobFailureHandlingListener = jobFailureHandlingListener;
	}

	/**
	 * Scheduler factory bean.
	 *
	 * @return the scheduler factory bean
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setJobFactory(springBeanJobFactory());
		schedulerFactoryBean.setGlobalJobListeners(jobFailureHandlingListener);
		return schedulerFactoryBean;
	}

	/**
	 * Spring bean job factory.
	 *
	 * @return the spring bean job factory
	 */
	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
		return new AutowiringSpringBeanJobFactoryConfig();
	}

}
