/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for LogFactory to verify that different logging implementations
 * can be configured and used correctly.
 */
class LogFactoryTest {

  private static final Class<?> TEST_LOGGER_CLASS = Object.class;
  private static final String CONFIG_FILE_PATH = "org/apache/ibatis/logging/mybatis-config.xml";

  @AfterAll
  static void restoreDefaultLoggingConfiguration() {
    // Reset to default SLF4J logging after all tests complete
    LogFactory.useSlf4jLogging();
  }

  @Test
  void shouldConfigureCommonsLoggingImplementation() {
    // Given: LogFactory is configured to use Commons Logging
    LogFactory.useCommonsLogging();
    
    // When: A logger is created
    Log logger = LogFactory.getLog(TEST_LOGGER_CLASS);
    
    // Then: The logger should be a Commons Logging implementation
    verifyLoggerFunctionality(logger);
    assertLoggerImplementation(logger, JakartaCommonsLoggingImpl.class);
  }

  @Test
  void shouldConfigureLog4JImplementation() {
    // Given: LogFactory is configured to use Log4J
    LogFactory.useLog4JLogging();
    
    // When: A logger is created
    Log logger = LogFactory.getLog(TEST_LOGGER_CLASS);
    
    // Then: The logger should be a Log4J implementation
    verifyLoggerFunctionality(logger);
    assertLoggerImplementation(logger, Log4jImpl.class);
  }

  @Test
  void shouldConfigureLog4J2Implementation() {
    // Given: LogFactory is configured to use Log4J2
    LogFactory.useLog4J2Logging();
    
    // When: A logger is created
    Log logger = LogFactory.getLog(TEST_LOGGER_CLASS);
    
    // Then: The logger should be a Log4J2 implementation
    verifyLoggerFunctionality(logger);
    assertLoggerImplementation(logger, Log4j2Impl.class);
  }

  @Test
  void shouldConfigureJdkLoggingImplementation() {
    // Given: LogFactory is configured to use JDK logging
    LogFactory.useJdkLogging();
    
    // When: A logger is created
    Log logger = LogFactory.getLog(TEST_LOGGER_CLASS);
    
    // Then: The logger should be a JDK logging implementation
    verifyLoggerFunctionality(logger);
    assertLoggerImplementation(logger, Jdk14LoggingImpl.class);
  }

  @Test
  void shouldConfigureSlf4jImplementation() {
    // Given: LogFactory is configured to use SLF4J
    LogFactory.useSlf4jLogging();
    
    // When: A logger is created
    Log logger = LogFactory.getLog(TEST_LOGGER_CLASS);
    
    // Then: The logger should be an SLF4J implementation
    verifyLoggerFunctionality(logger);
    assertLoggerImplementation(logger, Slf4jImpl.class);
  }

  @Test
  void shouldConfigureStdOutImplementation() {
    // Given: LogFactory is configured to use standard output logging
    LogFactory.useStdOutLogging();
    
    // When: A logger is created
    Log logger = LogFactory.getLog(TEST_LOGGER_CLASS);
    
    // Then: The logger should be a standard output implementation
    verifyLoggerFunctionality(logger);
    assertLoggerImplementation(logger, StdOutImpl.class);
  }

  @Test
  void shouldConfigureNoLoggingImplementation() {
    // Given: LogFactory is configured to disable logging
    LogFactory.useNoLogging();
    
    // When: A logger is created
    Log logger = LogFactory.getLog(TEST_LOGGER_CLASS);
    
    // Then: The logger should be a no-logging implementation
    verifyLoggerFunctionality(logger);
    assertLoggerImplementation(logger, NoLoggingImpl.class);
  }

  @Test
  void shouldReadLoggingImplementationFromConfigurationFile() throws Exception {
    // Given: A MyBatis configuration file that specifies logging implementation
    try (Reader configReader = Resources.getResourceAsReader(CONFIG_FILE_PATH)) {
      // When: SqlSessionFactory is built from the configuration
      new SqlSessionFactoryBuilder().build(configReader);
    }

    // Then: LogFactory should use the implementation specified in the config file
    Log logger = LogFactory.getLog(TEST_LOGGER_CLASS);
    logger.debug("Debug message from configuration test.");
    assertLoggerImplementation(logger, NoLoggingImpl.class);
  }

  /**
   * Exercises the logger with different log levels to ensure it functions correctly.
   * This helps verify that the logger implementation is working, not just instantiated.
   */
  private void verifyLoggerFunctionality(Log logger) {
    logger.warn("Test warning message");
    logger.debug("Test debug message");
    logger.error("Test error message");
    logger.error("Test error with exception", new Exception("Test exception for logging"));
  }

  /**
   * Asserts that the logger is of the expected implementation type.
   */
  private void assertLoggerImplementation(Log actualLogger, Class<?> expectedImplementationClass) {
    assertEquals(expectedImplementationClass.getName(), actualLogger.getClass().getName(),
        String.format("Expected logger implementation to be %s but was %s", 
            expectedImplementationClass.getSimpleName(), 
            actualLogger.getClass().getSimpleName()));
  }
}