package org.apache.ibatis.logging;

import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the dynamic selection of logging implementations in the LogFactory.
 */
@DisplayName("LogFactory Implementation Selection")
class LogFactorySelectionTest {

    /**
     * The LogFactory uses a static field to hold the chosen logging implementation.
     * This method resets it to the default (Slf4j) after all tests in this class run,
     * ensuring a clean state for other test suites.
     */
    @AfterAll
    static void resetLoggingImplementation() {
        LogFactory.useSlf4jLogging();
    }

    @Test
    @DisplayName("Should return a Log4j2Impl instance when Log4J2 is configured")
    void shouldReturnLog4j2ImplWhenLog4j2IsSelected() {
        // Arrange: Configure the factory to use the Log4J2 implementation.
        LogFactory.useLog4J2Logging();

        // Act: Request a logger from the factory.
        Log log = LogFactory.getLog(LogFactorySelectionTest.class);

        // Assert: Verify that the factory created an instance of the correct class.
        assertEquals(Log4j2Impl.class, log.getClass(), "The returned logger should be a Log4j2Impl instance.");
    }
}