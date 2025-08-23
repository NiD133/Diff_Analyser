package org.apache.ibatis.logging;

import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link LogFactory} to ensure it correctly configures the logging implementation.
 */
public class LogFactoryTest {

    /**
     * Verifies that after calling useNoLogging(), the factory provides a NoLoggingImpl instance.
     * This ensures that the logging can be successfully disabled.
     */
    @Test
    public void shouldProvideNoOpLoggerWhenNoLoggingIsSelected() {
        // Arrange: No specific setup is needed before the action.

        // Act: Configure the LogFactory to use the "no logging" implementation.
        LogFactory.useNoLogging();

        // Assert: The logger returned by the factory should be the no-operation implementation.
        Log log = LogFactory.getLog(this.getClass());
        assertTrue("LogFactory should return an instance of NoLoggingImpl", log instanceof NoLoggingImpl);
    }
}