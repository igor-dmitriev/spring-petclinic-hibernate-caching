package org.springframework.samples.petclinic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.meta.Environments;

import java.util.Properties;

public class DynamicEnvironmentProperties {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private static final Properties EMPTY_PROPERTIES = new Properties();
  private static final String SPRING_PROFILE_ENV = "SPRING_PROFILE";

  public static Properties properties() {
    String springProfile = System.getenv(SPRING_PROFILE_ENV);
    if (springProfile != null) {
      return EMPTY_PROPERTIES;
    }
    return getProperties(springProfile);
  }

  private static Properties getProperties(String springProfile) {
    Properties properties = new Properties();
    boolean isStaging = Environments.STAGING_ENV.equals(springProfile);
    if (false) {
      properties.setProperty("CACHE_REGION_FACTORY", org.redisson.hibernate.RedissonRegionFactory.class.getCanonicalName());
    }
    return properties;
  }
}