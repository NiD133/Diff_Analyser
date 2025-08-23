package org.apache.commons.lang3.concurrent;

import org.junit.Test;

/**
 * Unit tests for {@link BackgroundInitializer}.
 */
public class BackgroundInitializerTest {

    /**
     * Tests that calling get() on an initializer that has not been started
     * throws an IllegalStateException, as specified by the method's contract.
     */
    @Test(expected = IllegalStateException.class)
    public void getShouldThrowIllegalStateExceptionWhenNotStarted() {
        // Arrange: Create an initializer but do not call start().
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Act: Attempt to get the result before initialization has begun.
        // Assert: An IllegalStateException is expected, as declared by the @Test annotation.
        // The expected message is "start() must be called first!".
        initializer.get();
    }
}