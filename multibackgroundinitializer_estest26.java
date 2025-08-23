package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test class contains tests for the MultiBackgroundInitializer.
 * This specific test case was improved for clarity.
 */
public class MultiBackgroundInitializer_ESTestTest26 extends MultiBackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that the isStarted() flag remains false if the public start() method is never called,
     * even if initialize() and close() are invoked directly.
     */
    @Test(timeout = 4000)
    public void testIsStartedReturnsFalseWhenStartMethodIsNotCalled() throws Exception {
        // Arrange: Create a new MultiBackgroundInitializer instance.
        MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();

        // Act: Directly call the internal initialize() and close() methods.
        // The public start() method, which sets the 'started' state, is deliberately not called.
        multiInitializer.initialize();
        multiInitializer.close();

        // Assert: Verify that the initializer is not considered "started".
        assertFalse("The isStarted() flag should be false because start() was never invoked.",
                multiInitializer.isStarted());
    }
}