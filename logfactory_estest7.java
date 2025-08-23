package org.apache.ibatis.logging;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link LogFactory} class.
 * Note: This class name is retained from the original, but a name like 'LogFactoryTest' would be more conventional.
 */
public class LogFactory_ESTestTest7 {

    /**
     * Verifies that calling getLog() with a null logger name throws a RuntimeException.
     *
     * The LogFactory is expected to wrap the underlying constructor failure (an InvocationTargetException)
     * in a descriptive RuntimeException.
     */
    @Test
    public void shouldThrowRuntimeExceptionWhenLoggerNameIsNull() {
        // Arrange
        String expectedErrorMessage = "Error creating logger for logger null.  Cause: java.lang.reflect.InvocationTargetException";

        try {
            // Act
            LogFactory.getLog((String) null);

            // Assert: Fail the test if the expected exception was not thrown.
            fail("A RuntimeException should have been thrown for a null logger name.");

        } catch (RuntimeException e) {
            // Assert: Verify the exception is of the expected type and has the correct message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}