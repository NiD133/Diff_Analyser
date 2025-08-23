package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link MultiBackgroundInitializer}.
 */
public class MultiBackgroundInitializerTest {

    /**
     * Verifies that attempting to retrieve an initializer by a name that does not exist
     * from the results object throws a NoSuchElementException.
     */
    @Test
    public void getInitializerShouldThrowExceptionForUnknownName() throws Exception {
        // Arrange: Create a MultiBackgroundInitializer with no child initializers.
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer(executor);
        final String unknownInitializerName = "nonExistentInitializer";

        try {
            // Act: Initialize to get the results. Since no initializers were added,
            // the results will be empty.
            final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

            // Assert: Verify that an attempt to access a non-existent initializer fails as expected.
            final NoSuchElementException thrown = assertThrows(
                    NoSuchElementException.class,
                    () -> results.getInitializer(unknownInitializerName)
            );

            // Also, assert that the exception message is clear and helpful.
            final String expectedMessage = "No child initializer with name " + unknownInitializerName;
            assertEquals(expectedMessage, thrown.getMessage());

        } finally {
            // Clean up: Ensure the executor service is shut down to prevent resource leaks.
            executor.shutdown();
        }
    }
}