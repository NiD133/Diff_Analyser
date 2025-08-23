package org.apache.ibatis.logging;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import org.junit.Test;

/**
 * Test suite for the {@link LogFactory}.
 */
public class LogFactoryTest {

    /**
     * Verifies that calling {@code LogFactory.useLog4J2Logging()} throws a
     * {@code RuntimeException} when the Log4J2 library is not on the classpath.
     *
     * <p>This scenario simulates an environment where a user tries to configure Log4J2
     * logging without including the necessary dependency. The factory should fail gracefully
     * with a descriptive exception.
     */
    @Test
    public void useLog4J2Logging_shouldThrowRuntimeException_whenLog4J2LibraryIsMissing() {
        // Arrange: This test's environment is configured without the Log4J2 dependency.

        try {
            // Act: Attempt to set the logging implementation to Log4J2.
            LogFactory.useLog4J2Logging();

            // This line should not be reached.
            fail("Expected a RuntimeException because the Log4J2 implementation is not available.");

        } catch (RuntimeException e) {
            // Assert: Verify the exception is the one we expect.
            String expectedMessagePrefix = "Error setting Log implementation.";
            assertTrue(
                "Exception message should indicate a problem setting the log implementation.",
                e.getMessage().startsWith(expectedMessagePrefix)
            );

            Throwable cause = e.getCause();
            assertNotNull("The RuntimeException should have a cause.", cause);
            assertTrue(
                "The cause should be an InvocationTargetException, indicating a class loading or instantiation issue.",
                cause instanceof InvocationTargetException
            );
        }
    }
}