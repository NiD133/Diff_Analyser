package org.apache.ibatis.logging;

import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * Test suite for the {@link LogFactory}.
 * This refactored test focuses on a specific failure scenario.
 */
public class LogFactoryTest {

    /**
     * Verifies that LogFactory.useCustomLogging() throws a RuntimeException
     * when the provided logging implementation cannot be instantiated.
     * <p>
     * This scenario is simulated by using JakartaCommonsLoggingImpl without having
     * the actual Apache Commons Logging library on the classpath. The LogFactory
     * attempts to create an instance via reflection, which fails and results in
     * an InvocationTargetException, wrapped in a LogException (a RuntimeException).
     */
    @Test
    public void useCustomLoggingShouldThrowRuntimeExceptionWhenImplementationCannotBeInstantiated() {
        // Arrange: Select a logging implementation that is expected to fail instantiation
        // due to missing runtime dependencies (in this test's context).
        Class<JakartaCommonsLoggingImpl> logImplementationClass = JakartaCommonsLoggingImpl.class;

        // Act & Assert
        try {
            LogFactory.useCustomLogging(logImplementationClass);
            fail("Expected a RuntimeException because the custom log implementation is missing its required library.");
        } catch (RuntimeException e) {
            // Assert that the correct exception type and message are thrown.
            // The LogFactory wraps the underlying instantiation error.
            assertEquals("Error setting Log implementation.  Cause: java.lang.reflect.InvocationTargetException", e.getMessage());

            // Assert that the cause of the failure is indeed the reflection error.
            assertNotNull("The exception should have a cause.", e.getCause());
            assertTrue("The cause should be an InvocationTargetException.", e.getCause() instanceof InvocationTargetException);
        }
    }
}