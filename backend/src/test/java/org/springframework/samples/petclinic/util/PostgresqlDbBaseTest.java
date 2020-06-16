package org.springframework.samples.petclinic.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

public abstract class PostgresqlDbBaseTest {

  @BeforeEach
  public void before() {
    System.out.println("------------------------- START ----------------------------");
  }

  @AfterEach
  public void after() {
    System.out.println("------------------------- FINISH ----------------------------");
  }

  @Autowired
  protected DataSource dataSource;

  private static final PostgreSQLContainer<?> POSTGESQL_CONTAINER = new PostgreSQLContainer<>("postgres:9.6.1")
      .withUsername("test_login")
      .withPassword("test_password")
      .withDatabaseName("petclinic")
      .withExposedPorts(5432);

  static {
    POSTGESQL_CONTAINER.start();
    Runtime.getRuntime().addShutdownHook(new Thread(POSTGESQL_CONTAINER::stop));

    String urlPattern = "jdbc:p6spy:postgresql://%s:%s/petclinic";
    String url = String.format(urlPattern, POSTGESQL_CONTAINER.getContainerIpAddress(), POSTGESQL_CONTAINER.getMappedPort(5432));
    System.setProperty("spring.datasource.url", url);
    System.setProperty("spring.datasource.username", "test_login");
    System.setProperty("spring.datasource.password", "test_password");

  }
}