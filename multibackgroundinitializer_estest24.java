package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for {@link MultiBackgroundInitializer}.
 * This specific test was refactored for clarity from an auto-generated test case.
 */
public class MultiBackgroundInitializer_ESTestTest24 extends MultiBackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that calling getFuture() before start() throws an IllegalStateException.
     *
     * This test verifies that even after performing other operations like adding an
     * initializer or directly invoking the protected initialize() method, the
     * getFuture() method still correctly enforces its precondition that start()
     * must be called first.
     */
    @Test
    public void getFutureShouldThrowIllegalStateExceptionWhenStartIsNotCalled() throws Exception {
        // Arrange: Create a MultiBackgroundInitializer and add a child initializer.
        // We simulate a complex lifecycle by calling other methods, but crucially,
        // we never call the public start() method.
        MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        BackgroundInitializer<Object> childInitializer = new BackgroundInitializer<>();

        // Simulate a lifecycle without calling start(). Direct calls to the protected
        // initialize() method do not prepare the Future object that getFuture() expects.
        multiInitializer.initialize();
        multiInitializer.addInitializer("child", childInitializer);
        multiInitializer.close();
        multiInitializer.initialize();

        // Act & Assert: Expect an IllegalStateException when calling getFuture().
        try {
            multiInitializer.getFuture();
            fail("Expected an IllegalStateException because start() was not called.");
        } catch (IllegalStateException e) {
            // Verify the exception message is as expected.
            assertEquals("start() must be called first!", e.getMessage());
        }
    }
}