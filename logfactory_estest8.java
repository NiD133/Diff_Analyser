package org.apache.ibatis.logging;

import org.junit.Test;

/**
 * Test suite for {@link LogFactory}.
 * This class focuses on the factory's behavior when specific logging
 * provider libraries are not available in the runtime environment.
 */
public class LogFactoryTest {

    /**
     * Verifies that LogFactory.useCommonsLogging() throws a RuntimeException
     * when the Jakarta Commons Logging library is not available on the classpath.
     *
     * This test ensures that the factory correctly handles the absence of a required
     * logging dependency by failing fast with a clear exception.
     */
    @Test(expected = RuntimeException.class)
    public void shouldUseRuntimeExceptionWhenCommonsLoggingIsUnavailable() {
        // Arrange
        // The test environment is pre-configured without the Jakarta Commons Logging JAR.
        // This will cause the reflective instantiation within LogFactory to fail.

        // Act
        // Attempt to configure MyBatis to use Jakarta Commons Logging.
        LogFactory.useCommonsLogging();

        // Assert
        // The test passes if a RuntimeException is thrown, as declared by the
        // @Test(expected = ...) annotation. If no exception is thrown, the test will fail.
    }
}