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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for verifying LogFactory's ability to initialize different logging implementations.
 * <p>
 * The parameterized tests verify each supported logging implementation can be correctly
 * initialized. The separate configuration test verifies logging implementation can be
 * initialized from MyBatis configuration.
 * </p>
 */
class LogFactoryTest {

    /**
     * Resets LogFactory to default state after each test to ensure test isolation.
     */
    @AfterEach
    void resetLogFactory() {
        LogFactory.useSlf4jLogging();
    }

    /**
     * Provides test cases for verifying supported logging implementations.
     * <p>
     * Each test case consists of:
     * 1. Display name (for test reporting)
     * 2. Logging implementation setter method (Runnable)
     * 3. Expected implementation class
     * </p>
     * 
     * @return stream of test case arguments
     */
    private static Stream<Arguments> loggingImplementationProvider() {
        return Stream.of(
            Arguments.of("CommonsLogging", (Runnable) LogFactory::useCommonsLogging, JakartaCommonsLoggingImpl.class),
            Arguments.of("Log4J", (Runnable) LogFactory::useLog4JLogging, Log4jImpl.class),
            Arguments.of("Log4J2", (Runnable) LogFactory::useLog4J2Logging, Log4j2Impl.class),
            Arguments.of("JdkLogging", (Runnable) LogFactory::useJdkLogging, Jdk14LoggingImpl.class),
            Arguments.of("Slf4j", (Runnable) LogFactory::useSlf4jLogging, Slf4jImpl.class),
            Arguments.of("StdOut", (Runnable) LogFactory::useStdOutLogging, StdOutImpl.class),
            Arguments.of("NoLogging", (Runnable) LogFactory::useNoLogging, NoLoggingImpl.class)
        );
    }

    /**
     * Verifies LogFactory initializes the correct logging implementation.
     * 
     * @param displayName name of the logging implementation (for reporting)
     * @param setter method to activate the logging implementation
     * @param expectedLogClass expected logging implementation class
     */
    @ParameterizedTest(name = "shouldUse{0}")
    @MethodSource("loggingImplementationProvider")
    void shouldUseExpectedLoggingImplementation(String displayName, Runnable setter, Class<?> expectedLogClass) {
        // Activate logging implementation
        setter.run();
        
        // Retrieve logger and verify implementation
        Log log = LogFactory.getLog(Object.class);
        logSampleMessages(log);
        
        // Validate implementation class
        assertEquals(expectedLogClass.getName(), log.getClass().getName());
    }

    /**
     * Verifies LogFactory initializes logging implementation from configuration file.
     */
    @Test
    void shouldInitializeLogImplFromConfiguration() throws Exception {
        try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/logging/mybatis-config.xml")) {
            // Build session factory which parses configuration
            new SqlSessionFactoryBuilder().build(reader);
        }

        // Retrieve logger and verify no-logging implementation
        Log log = LogFactory.getLog(Object.class);
        log.debug("Debug message.");
        assertEquals(NoLoggingImpl.class.getName(), log.getClass().getName());
    }

    /**
     * Logs sample messages at different levels to verify logger functionality.
     * 
     * @param log logger instance to test
     */
    private void logSampleMessages(Log log) {
        log.warn("Sample warning message");
        log.debug("Sample debug message");
        log.error("Sample error message");
        log.error("Error with exception", new Exception("Test exception"));
    }
}