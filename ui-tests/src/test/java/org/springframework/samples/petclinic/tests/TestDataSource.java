package org.springframework.samples.petclinic.tests;

import com.p6spy.engine.spy.P6DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import static java.lang.String.format;

public abstract class TestDataSource {
  protected DataSource dataSource() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(format("jdbc:postgresql://%s:%d/petclinic", jdbcHost(), jdbcPort()));
    hikariConfig.setUsername("petclinic");
    hikariConfig.setPassword("q9KqUiu2vqnAuf");
    hikariConfig.setConnectionTestQuery("SELECT 1");
    hikariConfig.setMinimumIdle(3);
    hikariConfig.setMaximumPoolSize(10);
    return new P6DataSource(new HikariDataSource(hikariConfig));
  }

  protected abstract String jdbcHost();

  protected abstract int jdbcPort();
}
