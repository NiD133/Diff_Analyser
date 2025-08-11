package org.apache.ibatis.logging;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.params.provider.Named.named;

import java.io.Reader;
import java.util.stream.Stream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LogFactoryTest {

  // Reset global logging choice after each test to avoid cross-test interference.
  @AfterEach
  void resetLogFactory() {
    LogFactory.useSlf4jLogging();
  }

  @ParameterizedTest(name = "{index}: {0}")
  @MethodSource("knownImplementations")
  @DisplayName("LogFactory chooses the requested logging implementation")
  void usesExpectedImplementation(Runnable configure, Class<? extends Log> expectedImpl) {
    Log log = getConfiguredLogger(configure);
    assertSame(expectedImpl, log.getClass(), "LogFactory should use " + expectedImpl.getSimpleName());
  }

  // Provides all built-in implementations and how to activate them.
  @SuppressWarnings("deprecation") // useLog4JLogging() is intentionally tested for backward compatibility.
  static Stream<Arguments> knownImplementations() {
    return Stream.of(
        arguments(named("Commons Logging", (Runnable) LogFactory::useCommonsLogging), JakartaCommonsLoggingImpl.class),
        arguments(named("Log4j 1.x", (Runnable) LogFactory::useLog4JLogging), Log4jImpl.class),
        arguments(named("Log4j 2", (Runnable) LogFactory::useLog4J2Logging), Log4j2Impl.class),
        arguments(named("JDK logging (java.util.logging)", (Runnable) LogFactory::useJdkLogging), Jdk14LoggingImpl.class),
        arguments(named("SLF4J", (Runnable) LogFactory::useSlf4jLogging), Slf4jImpl.class),
        arguments(named("StdOut", (Runnable) LogFactory::useStdOutLogging), StdOutImpl.class),
        arguments(named("NoLogging", (Runnable) LogFactory::useNoLogging), NoLoggingImpl.class)
    );
  }

  @Test
  @DisplayName("Logging implementation can be set via mybatis-config.xml settings")
  void readsLogImplFromSettings() throws Exception {
    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/logging/mybatis-config.xml")) {
      new SqlSessionFactoryBuilder().build(reader);
    }
    Log log = LogFactory.getLog(Object.class);
    exerciseLogging(log);
    assertSame(NoLoggingImpl.class, log.getClass(), "Settings should select NoLoggingImpl");
  }

  // Helpers

  private static Log getConfiguredLogger(Runnable configure) {
    configure.run(); // Arrange
    Log log = LogFactory.getLog(Object.class); // Act
    exerciseLogging(log); // Sanity check that the logger is usable
    return log;
  }

  private static void exerciseLogging(Log log) {
    log.warn("Warning message.");
    log.debug("Debug message.");
    log.error("Error message.");
    log.error("Error with Exception.", new Exception("Test exception."));
  }
}