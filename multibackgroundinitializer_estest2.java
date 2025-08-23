package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutorService;

/**
 * Improved test for {@link MultiBackgroundInitializer}.
 * This version focuses on verifying specific behaviors in a clear and maintainable way.
 */
public class MultiBackgroundInitializer_ESTestTest2 extends MultiBackgroundInitializer_ESTest_scaffolding {

    /**
     * Verifies that calling the initialize() method a second time throws an IllegalStateException.
     * A MultiBackgroundInitializer is designed to be started only once.
     */
    @Test
    public void initializeShouldThrowIllegalStateExceptionWhenCalledTwice() throws Exception {
        // Arrange: Create a MultiBackgroundInitializer and add a child initializer.
        MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        BackgroundInitializer<String> childInitializer = new BackgroundInitializer<String>((ExecutorService) null) {
            @Override
            protected String initialize() {
                return "ChildResult";
            }
        };
        multiInitializer.addInitializer("child1", childInitializer);

        // Act: The first call to initialize() should succeed.
        multiInitializer.initialize();

        // Assert: The second call to initialize() should throw an exception.
        try {
            multiInitializer.initialize();
            fail("Expected an IllegalStateException to be thrown on the second call to initialize().");
        } catch (final IllegalStateException e) {
            // The exception is thrown by the child BackgroundInitializer when its start() method is called again.
            assertEquals("Cannot set ExecutorService after start()!", e.getMessage());
        }
    }
}