package org.apache.ibatis.logging;

import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link LogFactory} to ensure it correctly configures
 * different logging implementations.
 */
public class LogFactoryTest {

    /**
     * Verifies that calling LogFactory.useJdkLogging() correctly configures the factory
     * to produce loggers that are instances of Jdk14LoggingImpl.
     */
    @Test
    public void shouldSetJdkLoggingAsImplementationWhenRequested() {
        // Arrange: The LogFactory is a static utility, so no instance is needed.
        // The initial state is determined by the classpath and static initializers.

        // Act: Explicitly set the logging implementation to JDK Logging.
        LogFactory.useJdkLogging();

        // Assert: Verify that the logger created by the factory is the correct type.
        Log log = LogFactory.getLog(LogFactoryTest.class);

        assertNotNull("The logger instance should not be null.", log);
        assertTrue(
            "The logger should be an instance of Jdk14LoggingImpl after setting it.",
            log instanceof Jdk14LoggingImpl
        );
    }
}