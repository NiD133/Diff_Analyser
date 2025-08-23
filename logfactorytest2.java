package org.apache.ibatis.logging;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LogFactory Tests")
class LogFactoryTest {

  /**
   * After all tests in this class, restore the default logging implementation.
   * This prevents side effects on other tests that might run in the same suite.
   */
  @AfterAll
  static void restoreDefaultLogging() {
    LogFactory.useSlf4jLogging();
  }

  /**
   * A simple smoke test to ensure the created logger can be used without throwing exceptions.
   *
   * @param log The logger instance to exercise.
   */
  private void exerciseLogger(Log log) {
    log.warn("Warning message.");
    log.debug("Debug message.");
    log.error("Error message.");
    log.error("Error with Exception.", new Exception("Test exception."));
  }

  @Test
  @DisplayName("should return a Log4jImpl when Log4J logging is selected")
  void shouldSelectLog4jImplementation() {
    // Arrange: Explicitly select the Log4J logging implementation.
    LogFactory.useLog4JLogging();

    // Act: Request a logger from the factory.
    Log log = LogFactory.getLog(LogFactoryTest.class);

    // Assert: The factory should return an instance of the correct logger implementation.
    assertInstanceOf(Log4jImpl.class, log, "LogFactory should have returned a Log4jImpl instance.");

    // Verify: Ensure the logger is functional by calling its methods.
    // This confirms that the logger not only has the correct type but also works as expected.
    exerciseLogger(log);
  }
}