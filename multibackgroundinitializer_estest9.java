package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * This test was refactored from an automatically generated EvoSuite test case
 * to improve its clarity, focus, and maintainability.
 */
public class MultiBackgroundInitializer_ESTestTest9 {

    /**
     * Verifies that calling addInitializer() after start() has been invoked
     * throws an IllegalStateException. The initializer's state becomes locked
     * after starting, and no further modifications should be allowed.
     */
    @Test
    public void addInitializerShouldThrowIllegalStateExceptionAfterStart() {
        // Arrange: Create a MultiBackgroundInitializer and a child initializer to add.
        MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        BackgroundInitializer<Object> childInitializer = new BackgroundInitializer<>();

        // Act: Start the background processing. This action should lock the initializer
        // from accepting new child initializers.
        multiInitializer.start();

        // Assert: Attempting to add another initializer should fail.
        try {
            multiInitializer.addInitializer("childInitializer", childInitializer);
            fail("Expected an IllegalStateException because addInitializer() was called after start().");
        } catch (final IllegalStateException e) {
            // Verify the exception message is as expected.
            assertEquals("addInitializer() must not be called after start()!", e.getMessage());
        }
    }
}