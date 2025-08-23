package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link BackgroundInitializer}.
 */
public class BackgroundInitializerTest {

    /**
     * Tests that an initializer is not in the "initialized" state immediately
     * after being started, as the background task needs time to complete.
     */
    @Test
    public void isInitializedShouldReturnFalseImmediatelyAfterStart() {
        // Arrange: Create a basic BackgroundInitializer.
        // We provide a minimal implementation of the abstract initialize() method.
        // The actual result is not important for this test.
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<Object>() {
            @Override
            protected Object initialize() {
                // This background task is not expected to complete before the check.
                return new Object();
            }
        };

        // Act: Start the initializer and immediately check its status.
        final boolean startSucceeded = initializer.start();
        final boolean isInitialized = initializer.isInitialized();

        // Assert: Verify the state right after starting.
        // The start() method should return true on the first call.
        assertTrue("start() should return true on the first successful invocation.", startSucceeded);

        // Immediately after starting, the background task will not have completed yet,
        // so the initializer should not be considered initialized.
        assertFalse("isInitialized() should be false right after start(), before the task completes.", isInitialized);
    }
}