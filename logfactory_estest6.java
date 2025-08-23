package org.apache.ibatis.logging;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.junit.Test;

/**
 * Test suite for {@link LogFactory}.
 * This suite focuses on verifying the correct configuration of different logging implementations.
 */
public class LogFactoryTest {

    /**
     * Verifies that calling {@link LogFactory#useSlf4jLogging()} correctly
     * configures the factory to produce SLF4J logger instances.
     *
     * This test ensures that subsequent calls to {@link LogFactory#getLog(Class)}
     * return a logger of the expected type ({@link Slf4jImpl}).
     */
    @Test
    public void shouldSetSlf4jAsLoggingImplementation() {
        // Arrange: The initial state is managed by LogFactory's static initializer.
        // We are explicitly changing the logging implementation for this test.

        // Act: Set the logging implementation to SLF4J.
        LogFactory.useSlf4jLogging();
        Log log = LogFactory.getLog(this.getClass());

        // Assert: Verify that the factory now produces SLF4J loggers.
        assertNotNull("The created log instance should not be null.", log);
        assertThat("The log instance should be of type Slf4jImpl.", log, instanceOf(Slf4jImpl.class));
    }
}