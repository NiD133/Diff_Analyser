package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertNull;

import java.util.concurrent.ExecutorService;
import org.junit.Test;

/**
 * Unit tests for {@link BackgroundInitializer}.
 */
public class BackgroundInitializerTest {

    /**
     * Tests that getActiveExecutor() returns null if the initializer has not been started.
     */
    @Test
    public void testGetActiveExecutorShouldReturnNullBeforeStart() {
        // Arrange: Create a new BackgroundInitializer that has not been started.
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Act: Get the active executor.
        final ExecutorService activeExecutor = initializer.getActiveExecutor();

        // Assert: The executor should be null, as start() has not been called.
        assertNull("The active executor should be null before start() is called", activeExecutor);
    }
}