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

class LogFactoryTest {

  @AfterAll
  static void restoreDefaultLogging() {
    // Restore the default logging implementation after all tests
    LogFactory.useSlf4jLogging();
  }

  @Test
  void testCommonsLoggingImplementation() {
    verifyLoggingImplementation(JakartaCommonsLoggingImpl.class, LogFactory::useCommonsLogging);
  }

  @Test
  void testLog4JImplementation() {
    verifyLoggingImplementation(Log4jImpl.class, LogFactory::useLog4JLogging);
  }

  @Test
  void testLog4J2Implementation() {
    verifyLoggingImplementation(Log4j2Impl.class, LogFactory::useLog4J2Logging);
  }

  @Test
  void testJdkLoggingImplementation() {
    verifyLoggingImplementation(Jdk14LoggingImpl.class, LogFactory::useJdkLogging);
  }

  @Test
  void testSlf4jImplementation() {
    verifyLoggingImplementation(Slf4jImpl.class, LogFactory::useSlf4jLogging);
  }

  @Test
  void testStdOutLoggingImplementation() {
    verifyLoggingImplementation(StdOutImpl.class, LogFactory::useStdOutLogging);
  }

  @Test
  void testNoLoggingImplementation() {
    verifyLoggingImplementation(NoLoggingImpl.class, LogFactory::useNoLogging);
  }

  @Test
  void testLoggingImplementationFromConfig() throws Exception {
    // Load logging configuration from XML file
    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/logging/mybatis-config.xml")) {
      new SqlSessionFactoryBuilder().build(reader);
    }
    Log log = LogFactory.getLog(Object.class);
    log.debug("Debug message.");
    assertEquals(NoLoggingImpl.class.getName(), log.getClass().getName());
  }

  /**
   * Helper method to verify the logging implementation.
   *
   * @param expectedClass the expected logging implementation class
   * @param loggingSetup  the method to set up the logging implementation
   */
  private void verifyLoggingImplementation(Class<? extends Log> expectedClass, Runnable loggingSetup) {
    loggingSetup.run();
    Log log = LogFactory.getLog(Object.class);
    logTestMessages(log);
    assertEquals(expectedClass.getName(), log.getClass().getName());
  }

  /**
   * Logs various test messages using the provided log instance.
   *
   * @param log the log instance to use for logging messages
   */
  private void logTestMessages(Log log) {
    log.warn("Warning message.");
    log.debug("Debug message.");
    log.error("Error message.");
    log.error("Error with Exception.", new Exception("Test exception."));
  }
}