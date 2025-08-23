package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link BackgroundInitializer}.
 */
public class BackgroundInitializerTest {

    /**
     * Tests that getTypedException() correctly returns the same exception instance
     * that was passed to it, as per its contract.
     */
    @Test
    public void getTypedExceptionShouldReturnTheGivenException() {
        // Arrange
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();
        final ConcurrentException cause = new ConcurrentException("Test Exception");

        // Act
        final Exception result = initializer.getTypedException(cause);

        // Assert
        assertSame("The method should return the exact same exception instance.", cause, result);
    }
}