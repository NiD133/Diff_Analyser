package org.apache.ibatis.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LogFactory} focusing on its ability to select and configure
 * different logging implementations.
 */
class LogFactoryTest {

    /**
     * After all tests, restore the default logging implementation to prevent
     * side effects on other test suites.
     */
    @AfterAll
    static void restoreDefaultLoggingImplementation() {
        // SLF4J is the default logging implementation if it's on the classpath.
        // Resetting to it ensures a consistent state for other tests.
        LogFactory.useSlf4jLogging();
    }

    @Test
    void shouldReturnCommonsLoggerWhenCommonsLoggingIsSelected() {
        // Arrange: Explicitly select the Jakarta Commons Logging implementation.
        LogFactory.useCommonsLogging();

        // Act: Request a logger instance from the factory.
        Log log = LogFactory.getLog(LogFactoryTest.class);

        // Assert: Verify that the factory returned an instance of the correct logger wrapper.
        assertEquals(JakartaCommonsLoggingImpl.class, log.getClass());
    }
}