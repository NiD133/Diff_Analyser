package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.Delayed;
import org.junit.Test;

/**
 * This test case verifies the behavior of the BackgroundInitializer class,
 * specifically when its methods are called in an incorrect order.
 *
 * Note: The original test class name and hierarchy are preserved to maintain
 * structural consistency with the existing test suite.
 */
public class BackgroundInitializer_ESTestTest10 extends BackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that calling getFuture() before start() throws an IllegalStateException.
     * The contract of BackgroundInitializer requires start() to be called before
     * the Future result can be accessed.
     */
    @Test
    public void getFutureShouldThrowIllegalStateExceptionIfStartNotCalled() {
        // Arrange: Create a BackgroundInitializer instance that has not been started.
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();

        // Act & Assert: Attempt to get the Future and verify the expected exception.
        try {
            initializer.getFuture();
            fail("Expected an IllegalStateException because start() was not called.");
        } catch (final IllegalStateException e) {
            // Verify that the exception has the correct, specified message.
            assertEquals("start() must be called first!", e.getMessage());
        }
    }
}