package org.springframework.samples.petclinic.util;

import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.VncRecordingContainer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

public class VideoRecordingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private final GenericContainer targetContainer;

  private VncRecordingContainer vncRecordingContainer;
  private static final String FILE_NAME_PATTERN = "%s-%s-%s.flv";
  private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern("dd-MM-YYYY__HH-mm-ss");

  public VideoRecordingExtension(GenericContainer targetContainer) {
    this.targetContainer = targetContainer;
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    vncRecordingContainer = new VncRecordingContainer(targetContainer)
        .withVncPassword("secret")
        .withVncPort(5900);
    vncRecordingContainer.start();
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    try {
      context.getExecutionException().ifPresent(throwable -> handleFailedTest(context));
    } finally {
      vncRecordingContainer.stop();
    }
  }

  private void handleFailedTest(ExtensionContext context) {
    String fileName = String.format(
        FILE_NAME_PATTERN,
        context.getRequiredTestClass().getSimpleName(),
        context.getRequiredTestMethod().getName(),
        DATE_TIME_FORMATTER.format(LocalDateTime.now())
    );
    File path = new File(Configuration.reportsFolder + "/" + fileName);
    vncRecordingContainer.saveRecordingToFile(path);
  }
}