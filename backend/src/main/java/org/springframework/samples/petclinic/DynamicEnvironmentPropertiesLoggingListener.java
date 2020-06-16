package org.springframework.samples.petclinic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

public class DynamicEnvironmentPropertiesLoggingListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
  private final Logger log = LoggerFactory.getLogger(getClass());

  private static final String FORMAT_PATTERN = "{} : {}";

  private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
  private static final String SPRING_DATASOURCE_JDBC_URL = "spring.datasource.url";
  private static final String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
  private static final String CACHE_REGION_FACTORY = "spring.jpa.properties.hibernate.cache.region.factory_class";

  @Override
  public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
    log.info("--------------------- Application dynamic properties ------------------------------------------------------");
    Environment env = event.getEnvironment();
    log.info(FORMAT_PATTERN, SPRING_PROFILES_ACTIVE, env.getProperty(SPRING_PROFILES_ACTIVE));
    log.info(FORMAT_PATTERN, SPRING_DATASOURCE_JDBC_URL, env.getProperty(SPRING_DATASOURCE_JDBC_URL));
    log.info(FORMAT_PATTERN, SPRING_DATASOURCE_USERNAME, env.getProperty(SPRING_DATASOURCE_USERNAME));
    log.info(FORMAT_PATTERN, CACHE_REGION_FACTORY, env.getProperty(CACHE_REGION_FACTORY));
  }
}