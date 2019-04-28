package org.springframework.samples.petclinic.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.samples.petclinic.util.JmxUtil;
import org.springframework.samples.petclinic.util.TestContainerUtil;
import org.springframework.samples.petclinic.util.VideoRecordingExtension;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;

import io.qameta.allure.selenide.AllureSelenide;

public abstract class CiUiTest extends TestDataSource {
  private static DockerComposeContainer dockerComposeContainer = new DockerComposeContainer(new File("../docker-compose.yml"))
      .withLocalCompose(true)
      .withExposedService("application_1", 8080, Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(3)))
      .withExposedService("postgres_1", 5432);

  private static BrowserWebDriverContainer chrome;

  static {
    dockerComposeContainer.start();
    chrome = new BrowserWebDriverContainer()
        .withCapabilities(DesiredCapabilities.chrome());
    TestContainerUtil.linkContainersNetworks(dockerComposeContainer, chrome, "application_1");
    chrome.start();
    Configuration.baseUrl = "http://application:8080";
    WebDriverRunner.setWebDriver(chrome.getWebDriver());
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      JmxUtil.generateJacocoDump();
      dockerComposeContainer.stop();
      chrome.stop();
    }));
  }

  @BeforeEach
  void setUp() {
    SelenideLogger.addListener("allure", new AllureSelenide().savePageSource(false)); // tracing
  }

  @AfterEach
  void tearDown() {
    SelenideLogger.removeListener("allure");
  }

  @RegisterExtension
  public static VideoRecordingExtension videoRecordingExtension = new VideoRecordingExtension(chrome);

  @Override
  protected String jdbcHost() {
    return dockerComposeContainer.getServiceHost("postgres_1", 5432);
  }

  @Override
  protected int jdbcPort() {
    return dockerComposeContainer.getServicePort("postgres_1", 5432);
  }

  protected String homePath() {
    return "http://application:8080";
  }

  protected String apiLoginPath() {
    return "http://localhost:8070/login";
  }

}
