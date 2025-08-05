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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LogFactoryTest {

  /**
   * Resets the LogFactory to its default state (SLF4J) after each test.
   * This is crucial because tests modify the global static state of the factory.
   */
  @AfterEach
  void resetLogFactoryToDefault() {
    LogFactory.useSlf4jLogging();
  }

  @ParameterizedTest(name = "should use {0}")
  @MethodSource("logImplementations")
  void shouldSelectCorrectLogImplementationProgrammatically(String implementationName, Runnable setImplementation,
      Class<? extends Log> expectedLogClass) {
    // Arrange
    setImplementation.run();

    // Act
    Log log = LogFactory.getLog(LogFactoryTest.class);
    logSmokeTest(log);

    // Assert
    assertEquals(expectedLogClass, log.getClass());
  }

  private static Stream<Arguments> logImplementations() {
    return Stream.of(
        Arguments.of("SLF4J", (Runnable) LogFactory::useSlf4jLogging, Slf4jImpl.class),
        Arguments.of("Jakarta Commons Logging", (Runnable) LogFactory::useCommonsLogging, JakartaCommonsLoggingImpl.class),
        Arguments.of("Log4J", (Runnable) LogFactory::useLog4JLogging, Log4jImpl.class),
        Arguments.of("Log4J2", (Runnable) LogFactory::useLog4J2Logging, Log4j2Impl.class),
        Arguments.of("JDK Logging", (Runnable) LogFactory::useJdkLogging, Jdk14LoggingImpl.class),
        Arguments.of("Standard Out", (Runnable) LogFactory::useStdOutLogging, StdOutImpl.class),
        Arguments.of("No Logging", (Runnable) LogFactory::useNoLogging, NoLoggingImpl.class)
    );
  }

  @Test
  void shouldSelectLogImplFromMybatisConfiguration() throws Exception {
    // Arrange
    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/logging/mybatis-config.xml")) {
      // Act: Building the factory from a config file that specifies NoLoggingImpl
      new SqlSessionFactoryBuilder().build(reader);
    }

    Log log = LogFactory.getLog(LogFactoryTest.class);
    log.debug("Debug message after configuration.");

    // Assert
    assertEquals(NoLoggingImpl.class, log.getClass());
  }

  /**
   * A simple smoke test to ensure the log methods can be called without throwing an exception.
   * @param log The logger instance to test.
   */
  private void logSmokeTest(Log log) {
    log.warn("Warning message.");
    log.debug("Debug message.");
    log.error("Error message.");
    log.error("Error with Exception.", new Exception("Test exception."));
  }
}