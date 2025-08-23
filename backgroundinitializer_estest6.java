package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link BackgroundInitializer}.
 */
public class BackgroundInitializerTest {

    /**
     * Tests that the start() method is idempotent, meaning it can be called multiple
     * times without error, but only the first call actually starts the initialization.
     * Subsequent calls should have no effect and return false.
     */
    @Test
    public void startShouldBeIdempotentAndReturnFalseOnSubsequentCalls() throws Exception {
        // Arrange: Create a new BackgroundInitializer.
        // The type parameter <Object> is used as the actual initialized value is not relevant for this test.
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Act & Assert: First call to start()
        // The first call should successfully start the initializer and return true.
        boolean wasStarted = initializer.start();
        assertTrue("First call to start() should return true.", wasStarted);
        assertTrue("Initializer should be in a 'started' state.", initializer.isStarted());

        // Act & Assert: Second call to start()
        // A second call should be ignored and return false.
        wasStarted = initializer.start();
        assertFalse("Second call to start() should return false.", wasStarted);
        assertTrue("Initializer should remain in a 'started' state.", initializer.isStarted());

        // Act: Wait for the background task to complete to ensure the state is stable.
        initializer.get();

        // Assert: Final state check
        // The initializer should still be considered 'started' after completion.
        assertTrue("Initializer should remain 'started' after task completion.", initializer.isStarted());
    }
}