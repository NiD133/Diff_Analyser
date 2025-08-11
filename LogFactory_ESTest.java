package org.apache.ibatis.logging;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import org.apache.ibatis.logging.jdbc.JdbcTracer;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link LogFactory}.
 * This class focuses on verifying the factory's ability to select, configure,
 * and create logger instances.
 */
public class LogFactoryTest {

  /**
   * Resets the LogFactory's static state before each test.
   * This is crucial because LogFactory uses a static field to hold the chosen
   * logging implementation, and we need to ensure test isolation.
   */
  @Before
  public void resetLogFactory() throws Exception {
    Field logConstructorField = LogFactory.class.getDeclaredField("logConstructor");
    logConstructorField.setAccessible(true);
    logConstructorField.set(null, null);
  }

  @Test
  public void shouldGetLogForClass() {
    Log log = LogFactory.getLog(LogFactoryTest.class);
    assertNotNull("Log should not be null", log);
  }

  @Test
  public void shouldGetLogForName() {
    Log log = LogFactory.getLog("org.apache.ibatis.logging.LogFactoryTest");
    assertNotNull("Log should not be null", log);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNPEWhenGettingLogForNullClass() {
    LogFactory.getLog((Class<?>) null);
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowRuntimeExceptionWhenGettingLogForNullName() {
    // This test relies on the default log implementation (e.g., Slf4jImpl)
    // whose constructor does not accept a null argument, causing an
    // InvocationTargetException that LogFactory wraps in a RuntimeException.
    LogFactory.useSlf4jLogging(); // Ensure a known constructor is set
    LogFactory.getLog((String) null);
  }

  @Test
  public void shouldUseSlf4jLogging() {
    LogFactory.useSlf4jLogging();
    Log log = LogFactory.getLog(LogFactoryTest.class);
    assertTrue("Log should be an instance of Slf4jImpl", log instanceof Slf4jImpl);
  }

  @Test
  public void shouldUseJdkLogging() {
    LogFactory.useJdkLogging();
    Log log = LogFactory.getLog(LogFactoryTest.class);
    // The Jdk14LoggingImpl is wrapped by another log type for tracing
    assertTrue("Log should be an instance of Jdk14LoggingImpl", log instanceof JdbcTracer);
  }

  @Test
  public void shouldUseStdOutLogging() {
    LogFactory.useStdOutLogging();
    Log log = LogFactory.getLog(LogFactoryTest.class);
    assertTrue("Log should be an instance of StdOutImpl", log instanceof StdOutImpl);
  }

  @Test
  public void shouldUseNoLogging() {
    LogFactory.useNoLogging();
    Log log = LogFactory.getLog(LogFactoryTest.class);
    assertTrue("Log should be an instance of NoLoggingImpl", log instanceof NoLoggingImpl);
  }

  @Test(expected = RuntimeException.class)
  public void shouldFailToUseLog4jWhenLibraryIsUnavailable() {
    // This test expects a RuntimeException because the Log4j library
    // is not on the test classpath.
    LogFactory.useLog4JLogging();
  }

  @Test(expected = RuntimeException.class)
  public void shouldFailToUseLog4j2WhenLibraryIsUnavailable() {
    // This test expects a RuntimeException because the Log4j2 library
    // is not on the test classpath.
    LogFactory.useLog4J2Logging();
  }

  @Test(expected = RuntimeException.class)
  public void shouldFailToUseCommonsLoggingWhenLibraryIsUnavailable() {
    // This test expects a RuntimeException because the Apache Commons Logging
    // library is not on the test classpath.
    LogFactory.useCommonsLogging();
  }

  @Test
  public void shouldUseCustomLoggingImplementation() {
    LogFactory.useCustomLogging(CustomLogImpl.class);
    Log log = LogFactory.getLog(LogFactoryTest.class);
    assertTrue("Log should be an instance of CustomLogImpl", log instanceof CustomLogImpl);
  }

  @Test(expected = RuntimeException.class)
  public void shouldFailWhenCustomLoggerHasNoValidConstructor() {
    // This test ensures that if a custom logger does not have a constructor
    // that accepts a String, a RuntimeException is thrown.
    LogFactory.useCustomLogging(InvalidCustomLogImpl.class);
  }

  /** A valid custom Log implementation for testing. */
  public static class CustomLogImpl implements Log {
    public CustomLogImpl(String logger) {
      // Constructor required by LogFactory
    }
    @Override public boolean isDebugEnabled() { return false; }
    @Override public boolean isTraceEnabled() { return false; }
    @Override public void error(String s, Throwable e) {}
    @Override public void error(String s) {}
    @Override public void debug(String s) {}
    @Override public void trace(String s) {}
    @Override public void warn(String s) {}
  }

  /** An invalid custom Log implementation without the required constructor. */
  public static class InvalidCustomLogImpl implements Log {
    public InvalidCustomLogImpl() {
      // No constructor that accepts a String argument
    }
    @Override public boolean isDebugEnabled() { return false; }
    @Override public boolean isTraceEnabled() { return false; }
    @Override public void error(String s, Throwable e) {}
    @Override public void error(String s) {}
    @Override public void debug(String s) {}
    @Override public void trace(String s) {}
    @Override public void warn(String s) {}
  }
}