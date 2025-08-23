package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test suite for {@link BackgroundInitializer}.
 */
public class BackgroundInitializerTest {

    /**
     * Tests that calling get() before start() results in an IllegalStateException.
     * The initializer must be started before its result can be retrieved.
     */
    @Test
    public void getShouldThrowIllegalStateExceptionWhenCalledBeforeStart() {
        // Arrange: Create an initializer but do not start it.
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Act & Assert: Expect an IllegalStateException when get() is called.
        try {
            initializer.get();
            fail("Expected an IllegalStateException because start() was not called.");
        } catch (final IllegalStateException e) {
            // This is the expected outcome.
            // Verify that the exception has the correct message.
            assertEquals("start() must be called first!", e.getMessage());
        } catch (final ConcurrentException e) {
            // Fail the test if an unexpected checked exception is caught.
            fail("Caught an unexpected ConcurrentException: " + e.getMessage());
        }
    }
}