package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link MultiBackgroundInitializer.MultiBackgroundInitializerResults} class.
 */
public class MultiBackgroundInitializerTest {

    /**
     * Tests that calling isException() on a results object with a name that does not
     * correspond to any child initializer throws a NoSuchElementException.
     */
    @Test
    public void isExceptionShouldThrowExceptionForUnknownInitializer() {
        // Arrange: Create a MultiBackgroundInitializer with no child initializers.
        // This results in an empty MultiBackgroundInitializerResults object.
        MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Define a name for an initializer that has not been added.
        // The original auto-generated test used an empty string for this case.
        final String unknownInitializerName = "";

        // Act & Assert: Verify that an exception is thrown when querying a non-existent initializer.
        try {
            results.isException(unknownInitializerName);
            fail("Expected a NoSuchElementException to be thrown for an unknown initializer name.");
        } catch (final NoSuchElementException e) {
            // This is the expected behavior.
            // For a more robust test, we also verify the exception's message.
            final String expectedMessage = "No child initializer with name " + unknownInitializerName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}