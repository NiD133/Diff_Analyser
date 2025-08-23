package org.apache.ibatis.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * This test suite verifies the configuration of the LogFactory.
 * It ensures that when a specific logging implementation is requested,
 * the factory provides a logger of the correct type.
 */
class LogFactoryConfigurationTest {

  /**
   * After all tests, reset the logging implementation to the default (Slf4j).
   * This prevents side effects in other test suites that might rely on the
   * default auto-discovery behavior of the LogFactory.
   */
  @AfterAll
  static void resetLoggingToDefault() {
    LogFactory.useSlf4jLogging();
  }

  @Test
  void shouldSelectJdkLoggingImplementation() {
    // Arrange: Force the LogFactory to use the JDK logging implementation.
    LogFactory.useJdkLogging();

    // Act: Request a logger instance from the factory.
    Log log = LogFactory.getLog(LogFactoryConfigurationTest.class);

    // Assert: Verify that the returned logger is of the expected type.
    assertEquals(Jdk14LoggingImpl.class, log.getClass(),
        "The LogFactory should have returned an instance of Jdk14LoggingImpl.");
  }
}