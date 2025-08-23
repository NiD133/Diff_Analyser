package org.apache.ibatis.logging;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link LogFactory}.
 *
 * Note: This class extends a scaffolding class, which may handle
 * setup and teardown, such as resetting the LogFactory's state
 * between tests.
 */
public class LogFactoryTest extends LogFactory_ESTest_scaffolding {

    /**
     * Verifies that LogFactory.getLog(Class) returns a valid, non-null Log instance.
     *
     * This is a fundamental contract of the factory. It should always provide a logger,
     * even if it's a "no-operation" logger, to prevent NullPointerExceptions in the
     * application code that uses it.
     */
    @Test
    public void shouldReturnNonNullLogWhenGettingLogByClass() {
        // Arrange: Define the class for which a logger is requested.
        // Using Object.class is a simple and generic choice.
        Class<?> loggerClass = Object.class;

        // Act: Request a Log instance from the factory.
        Log log = LogFactory.getLog(loggerClass);

        // Assert: Ensure the factory returned a non-null logger instance.
        assertNotNull("The LogFactory should never return a null logger.", log);
    }
}