package org.apache.ibatis.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test suite for {@link LogFactory}.
 * This class focuses on testing the factory's behavior when underlying logging libraries are not available.
 */
public class LogFactoryTest {

    /**
     * Verifies that calling useLog4JLogging() throws a RuntimeException
     * when the Log4J library is not present on the classpath.
     *
     * The LogFactory should fail gracefully with a clear error message
     * if it cannot find the required classes for a specific logging implementation.
     */
    @Test
    public void shouldUseLog4JLoggingShouldThrowExceptionWhenLog4JIsMissing() {
        try {
            // Attempt to set the logging implementation to Log4J.
            // This is expected to fail in a test environment where Log4J is not a dependency.
            LogFactory.useLog4JLogging();

            // If this line is reached, it means no exception was thrown, which is an error.
            fail("Expected a RuntimeException because the Log4J library is not on the classpath.");

        } catch (RuntimeException e) {
            // 1. Verify the exception message is informative.
            String expectedMessage = "Error setting Log implementation.  Cause: java.lang.NoClassDefFoundError: org/apache/log4j/Priority";
            assertEquals(expectedMessage, e.getMessage());

            // 2. Verify the root cause is the expected NoClassDefFoundError.
            // This confirms the failure is for the correct reason (a missing class).
            Throwable cause = e.getCause();
            assertNotNull("The exception should have a cause.", cause);
            assertTrue("The cause should be a NoClassDefFoundError.", cause instanceof NoClassDefFoundError);
        }
    }
}