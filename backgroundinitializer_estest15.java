package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link BackgroundInitializer}.
 */
public class BackgroundInitializerTest {

    /**
     * Tests that calling get() on an initializer that has not been started
     * throws an IllegalStateException.
     */
    @Test
    public void getShouldThrowIllegalStateExceptionWhenNotStarted() {
        // Arrange: Create a BackgroundInitializer that has not been started.
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Act & Assert: Attempt to get the result and verify the correct exception is thrown.
        try {
            initializer.get();
            fail("Expected an IllegalStateException because start() was not called.");
        } catch (final IllegalStateException e) {
            // This is the expected outcome.
            assertEquals("start() must be called first!", e.getMessage());
        } catch (final ConcurrentException e) {
            // The get() method declares ConcurrentException, but it should not be thrown
            // in this scenario. We fail the test if it is.
            fail("A ConcurrentException should not be thrown when the initializer is not started: " + e);
        }
    }
}