package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class focuses on the behavior of the MultiBackgroundInitializer.MultiBackgroundInitializerResults class.
 * The original test class name, MultiBackgroundInitializer_ESTestTest22, is kept for context,
 * but a more descriptive name like "MultiBackgroundInitializerResultsTest" would be preferable.
 */
public class MultiBackgroundInitializer_ESTestTest22 {

    /**
     * Verifies that calling getException() with a null name on the results of an
     * initializer that has no children throws a NoSuchElementException.
     */
    @Test
    public void getExceptionWithNullNameOnEmptyResultsThrowsNoSuchElementException() throws Exception {
        // Arrange: Create a MultiBackgroundInitializer with no child initializers
        // and retrieve its results object.
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.initialize();

        // Act & Assert: Attempt to get an exception for a null initializer name.
        try {
            results.getException(null);
            fail("Expected a NoSuchElementException to be thrown, but it was not.");
        } catch (final NoSuchElementException e) {
            // Verify the exception message to confirm the cause of the failure.
            assertEquals("No child initializer with name null", e.getMessage());
        }
    }
}