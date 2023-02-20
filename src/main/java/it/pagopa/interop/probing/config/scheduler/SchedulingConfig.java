/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : Feb 16, 2023
* Author      : dxc technology
* Project Name: interop-be-probing 
* Package     : it.pagopa.interop.probing.config
* File Name   : SchedulingConfig.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.config.scheduler;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import it.pagopa.interop.probing.listener.JobFailureHandlingListener;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class SchedulingConfig.
 */
@Configuration

/** The Constant log. */
@Slf4j
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
		schedulerFactoryBean.setQuartzProperties(quartzProperties());
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
	
	/**
	 * Quartz properties.
	 *
	 * @return the properties
	 */
	@Bean
	public Properties quartzProperties() {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		Properties properties = null;
		try {
			propertiesFactoryBean.afterPropertiesSet();
			properties = propertiesFactoryBean.getObject();

		} catch (IOException e) {
			log.warn("Cannot load quartz.properties.");
		}

		return properties;
	}

}
