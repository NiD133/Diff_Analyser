package org.apache.commons.lang3.concurrent;

import org.apache.commons.lang3.concurrent.MultiBackgroundInitializer.MultiBackgroundInitializerResults;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link MultiBackgroundInitializer}.
 */
public class MultiBackgroundInitializerTest {

    /**
     * Tests that the MultiBackgroundInitializer can successfully initialize a child task,
     * report a successful result, and be closed without errors.
     */
    @Test
    public void shouldInitializeAndCloseSuccessfullyWithOneChild() throws Exception {
        // Arrange: Create a MultiBackgroundInitializer and a simple child initializer to run.
        final MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        final String childTaskName = "childTask1";

        // This child initializer simply returns a string upon successful completion.
        final BackgroundInitializer<String> childInitializer = new BackgroundInitializer<String>() {
            @Override
            protected String initialize() {
                return "Child task complete";
            }
        };

        multiInitializer.addInitializer(childTaskName, childInitializer);

        // Act: Start the background initialization process.
        final MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Assert: Verify that the initialization completed successfully and the state is correct.
        assertNotNull("The results object should not be null.", results);
        assertTrue("The overall initialization should be successful.", results.isSuccessful());
        assertTrue("The multi-initializer should be marked as initialized after completion.", multiInitializer.isInitialized());
        assertNotNull("The result from the child task should be available.", results.getResultObject(childTaskName));

        // Act & Assert: Closing the initializer should not throw an exception.
        // The test method will fail if close() throws an unexpected exception.
        multiInitializer.close();
    }
}