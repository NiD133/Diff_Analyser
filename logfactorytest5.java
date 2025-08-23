package org.apache.ibatis.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * Tests the LogFactory's ability to explicitly configure a logging implementation.
 * This suite focuses on the selection of SLF4J.
 */
class LogFactorySelectionTest {

  /**
   * The LogFactory maintains the chosen logging implementation in a static field.
   * This method resets the factory to a known state after tests in this class run,
   * preventing side effects on other test suites.
   */
  @AfterAll
  static void resetLoggingImplementation() {
    LogFactory.useSlf4jLogging();
  }

  /**
   * Verifies that when SLF4J logging is explicitly requested, the LogFactory
   * provides an instance of Slf4jImpl.
   */
  @Test
  void shouldProvideSlf4jLoggerWhenExplicitlyConfigured() {
    // Arrange: Explicitly configure the LogFactory to use SLF4J.
    LogFactory.useSlf4jLogging();

    // Act: Request a logger instance and use it.
    Log log = LogFactory.getLog(LogFactorySelectionTest.class);
    exerciseLog(log);

    // Assert: The returned logger should be the SLF4J implementation.
    assertEquals(Slf4jImpl.class, log.getClass());
  }

  /**
   * Helper method to exercise the logger instance by logging messages at various levels.
   * This confirms the logger is functional.
   *
   * @param log The logger instance to use.
   */
  private void exerciseLog(Log log) {
    log.warn("Warning message.");
    log.debug("Debug message.");
    log.error("Error message.");
    log.error("Error with Exception.", new Exception("Test exception."));
  }
}