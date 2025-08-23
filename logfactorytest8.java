package org.apache.ibatis.logging;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the configuration of the LogFactory from mybatis-config.xml.
 */
@DisplayName("LogFactory Configuration")
class LogFactoryConfigurationTest {

  /**
   * The LogFactory maintains a static reference to the logging implementation.
   * This method resets it to the default after all tests in this class run,
   * ensuring test isolation and preventing side effects on other tests.
   */
  @AfterAll
  static void restoreDefaultLoggingImplementation() {
    LogFactory.useSlf4jLogging();
  }

  @Test
  @DisplayName("Should use log implementation specified in mybatis-config.xml")
  void shouldUseLogImplementationFromConfigFile() throws Exception {
    // Arrange: The mybatis-config.xml file is expected to contain a setting
    // that configures the logging implementation, e.g., <setting name="logImpl" value="NO_LOGGING"/>
    String configFile = "org/apache/ibatis/logging/mybatis-config.xml";

    // Act: Building the SqlSessionFactory from an XML configuration file triggers
    // the configuration of the static LogFactory based on the settings in that file.
    try (Reader reader = Resources.getResourceAsReader(configFile)) {
      new SqlSessionFactoryBuilder().build(reader);
    }

    // Assert: Verify that the LogFactory now uses the implementation specified in the config.
    // In this case, we expect it to be configured to use NoLoggingImpl.
    Log log = LogFactory.getLog(LogFactoryConfigurationTest.class);
    assertInstanceOf(NoLoggingImpl.class, log,
        "LogFactory should have been configured to use NoLoggingImpl from the XML config.");
  }
}