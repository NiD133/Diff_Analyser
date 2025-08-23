package org.apache.ibatis.logging;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Verifies that the {@link LogFactory} can be explicitly configured to use
 * standard output logging.
 */
@DisplayName("LogFactory - Standard Output Logging Configuration")
class LogFactoryStdOutTest {

  /**
   * The LogFactory maintains the logging implementation in a static field.
   * To prevent side effects on other tests, we reset it to a known state
   * after all tests in this class have run.
   */
  @AfterAll
  static void resetLogFactory() {
    // The default auto-detection mechanism prefers Slf4j, so we reset to that.
    LogFactory.useSlf4jLogging();
  }

  @Test
  @DisplayName("Should provide a StdOutImpl logger when configured for standard output")
  void shouldProvideStdOutLoggerWhenConfigured() {
    // Arrange: Configure the factory to use the standard output logger.
    LogFactory.useStdOutLogging();

    // Act: Request a logger instance from the factory.
    Log log = LogFactory.getLog(LogFactoryStdOutTest.class);

    // Assert: The factory should return an instance of the standard output logger implementation.
    assertInstanceOf(StdOutImpl.class, log,
        "LogFactory should provide a StdOutImpl instance after useStdOutLogging() is called.");
  }
}