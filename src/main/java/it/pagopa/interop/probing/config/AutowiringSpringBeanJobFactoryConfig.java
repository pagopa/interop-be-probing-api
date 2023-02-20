/*
 * 
 */
package it.pagopa.interop.probing.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * The Class AutowiringSpringBeanJobFactoryConfig.
 *
 */
public final class AutowiringSpringBeanJobFactoryConfig extends SpringBeanJobFactory
		implements ApplicationContextAware {

	/** The bean factory. */
	private AutowireCapableBeanFactory beanFactory;

	/**
	 * Sets the application context.
	 *
	 * @param context the new application context
	 */
	@Override
	public void setApplicationContext(final ApplicationContext context) {
		beanFactory = context.getAutowireCapableBeanFactory();
	}

	/**
	 * Creates the job instance.
	 *
	 * @param bundle the bundle
	 * @return the object
	 * @throws Exception the exception
	 */
	@Override
	protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
		final Object job = super.createJobInstance(bundle);
		beanFactory.autowireBean(job);
		return job;
	}
}