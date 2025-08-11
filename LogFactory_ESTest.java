import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for LogFactory that avoid environment-dependent behavior and
 * focus on clear, deterministic expectations.
 *
 * Notes:
 * - Tests do not assert behavior for optional integrations (e.g., Log4J 1.x, Log4J2, Commons Logging),
 *   as those may or may not be on the classpath and would make tests flaky.
 * - Each test ensures the selected logging implementation (when relevant) before performing assertions.
 */
class LogFactoryTest {

  @Test
  @DisplayName("getLog(Class) returns a logger instance")
  void getLog_byClass_returnsLogger() {
    // when
    Log log = LogFactory.getLog(LogFactory.class);

    // then
    assertNotNull(log, "Expected a non-null logger");
  }

  @Test
  @DisplayName("getLog(String) returns a logger instance")
  void getLog_byName_returnsLogger() {
    // when
    Log log = LogFactory.getLog("org.apache.ibatis.logging.LogFactoryTest");

    // then
    assertNotNull(log, "Expected a non-null logger");
  }

  @Test
  @DisplayName("getLog(Class) rejects null with NullPointerException")
  void getLog_byClass_null_throwsNPE() {
    // expect
    assertThrows(NullPointerException.class, () -> LogFactory.getLog((Class<?>) null));
  }

  @Test
  @DisplayName("Switching to StdOut logging succeeds and subsequent getLog works")
  void useStdOutLogging_thenGetLogWorks() {
    // given
    assertDoesNotThrow(LogFactory::useStdOutLogging, "Switching to StdOut logging should not throw");

    // when
    Log log = LogFactory.getLog(getClass());

    // then
    assertNotNull(log, "Expected a non-null logger after switching to StdOut logging");
  }

  @Test
  @DisplayName("Switching to JDK logging succeeds and subsequent getLog works")
  void useJdkLogging_thenGetLogWorks() {
    // given
    assertDoesNotThrow(LogFactory::useJdkLogging, "Switching to JDK logging should not throw");

    // when
    Log log = LogFactory.getLog(LogFactoryTest.class);

    // then
    assertNotNull(log, "Expected a non-null logger after switching to JDK logging");
  }

  @Test
  @DisplayName("Switching to No logging succeeds and subsequent getLog works")
  void useNoLogging_thenGetLogWorks() {
    // given
    assertDoesNotThrow(LogFactory::useNoLogging, "Switching to No logging should not throw");

    // when
    Log log = LogFactory.getLog("any.logger.name");

    // then
    assertNotNull(log, "Expected a non-null logger after switching to No logging");
  }
}