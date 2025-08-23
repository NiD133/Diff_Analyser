package org.apache.ibatis.logging;

import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Tests for the {@link LogFactory} to verify its dynamic logging implementation selection.
 */
@DisplayName("LogFactory Configuration")
class LogFactoryTest {

  /**
   * Restores the default logging implementation after all tests in this class have run.
   * This prevents side effects on other test classes.
   */
  @AfterAll
  static void restoreDefaultLogging() {
    LogFactory.useSlf4jLogging();
  }

  @Test
  @DisplayName("Should return NoLoggingImpl when no-logging mode is enabled")
  void getLog_shouldReturnNoLoggingImpl_whenNoLoggingIsSelected() {
    // Arrange: Explicitly select the 'no logging' implementation.
    LogFactory.useNoLogging();

    // Act: Request a logger instance from the factory.
    Log log = LogFactory.getLog(LogFactoryTest.class);

    // Assert:
    // 1. The factory should return an instance of the correct logger implementation.
    assertEquals(NoLoggingImpl.class, log.getClass(),
        "LogFactory should provide a NoLoggingImpl instance.");

    // 2. The NoLoggingImpl instance should be a functioning no-op logger.
    //    Verify that calling its methods does not throw any exceptions.
    assertDoesNotThrow(() -> {
      log.warn("This is a warning.");
      log.debug("This is a debug message.");
      log.error("This is an error.");
      log.error("This is an error with an exception.", new Exception("Test exception"));
    }, "NoLoggingImpl methods should not throw exceptions.");
  }
}