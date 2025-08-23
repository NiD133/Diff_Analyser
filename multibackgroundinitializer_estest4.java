package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for {@link MultiBackgroundInitializer}.
 */
public class MultiBackgroundInitializerTest {

    /**
     * Tests that initialization can still succeed for a child initializer that has its own
     * external executor, even after the parent MultiBackgroundInitializer has been closed.
     * Closing the parent shuts down its internal executor, but should not affect children
     * that do not rely on it.
     */
    @Test
    public void initializeSucceedsForChildWithOwnExecutorEvenAfterParentIsClosed() throws Exception {
        // Arrange
        final String childInitializerName = "childWithOwnExecutor";
        final MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();

        // Create a child initializer with its own dedicated executor service.
        // The default initialize() for a raw BackgroundInitializer simply returns null.
        final ExecutorService childExecutor = Executors.newSingleThreadExecutor();
        final BackgroundInitializer<Object> childInitializer = new BackgroundInitializer<>(childExecutor);
        multiInitializer.addInitializer(childInitializerName, childInitializer);

        // Act
        // Close the parent initializer *before* starting it. This creates and then
        // immediately shuts down the parent's internal executor service.
        // A second call is made to ensure the close() operation is idempotent.
        multiInitializer.close();
        multiInitializer.close();

        // Directly invoke the protected initialize() method for this white-box test.
        // This is expected to succeed because the child uses its own executor,
        // which was not affected by the parent's close() call.
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Assert
        assertTrue("Overall initialization should be successful.", results.isSuccessful());
        assertFalse("Child initializer should not have thrown an exception.", results.isException(childInitializerName));
        assertNull("Result from the child initializer should be null, as per its default implementation.", results.getResultObject(childInitializerName));

        // Cleanup
        childExecutor.shutdown();
    }
}