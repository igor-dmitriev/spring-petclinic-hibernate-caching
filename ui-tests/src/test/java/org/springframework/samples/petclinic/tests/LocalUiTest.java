package org.springframework.samples.petclinic.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import io.qameta.allure.selenide.AllureSelenide;

public abstract class LocalUiTest extends TestDataSource {
  @BeforeAll
  public static void setUpClass() {
    Configuration.baseUrl = "http://localhost:3000";
  }

  @BeforeEach
  void setUp() {
    SelenideLogger.addListener("allure", new AllureSelenide().savePageSource(false)); // tracing
  }

  @AfterEach
  void tearDown() {
    SelenideLogger.removeListener("allure");
  }

  @Override
  protected String jdbcHost() {
    return "127.0.0.1";
  }

  @Override
  protected int jdbcPort() {
    return 5432;
  }

  protected String homePath() {
    return "http://localhost:3000";
  }

  protected String apiLoginPath() {
    return "http://localhost:8080/login";
  }
}
