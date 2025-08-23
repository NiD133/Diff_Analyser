package org.apache.ibatis.logging;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Tests for the {@link LogFactory}.
 */
public class LogFactoryTest {

    /**
     * The LogFactory is designed to fall back to a default logging implementation
     * (like NoLoggingImpl) if no other logging frameworks are found on the classpath.
     * This test verifies that getLog() always returns a valid, non-null Log instance,
     * ensuring the fallback mechanism works as expected.
     */
    @Test
    public void shouldReturnNonNullLoggerForGivenName() {
        // Arrange
        String loggerName = LogFactory.class.getName();

        // Act
        Log logger = LogFactory.getLog(loggerName);

        // Assert
        assertNotNull("LogFactory.getLog() should never return null.", logger);
    }
}