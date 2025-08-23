package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import org.junit.Test;

/**
 * This test class has been improved for understandability.
 * The original test was auto-generated and contained irrelevant setup code,
 * unclear variable names, and a non-standard structure.
 * This version focuses on a single, clear scenario.
 */
public class MultiBackgroundInitializer_ESTestTest3 {

    /**
     * Tests that attempting to retrieve an initializer by a non-existent name from the
     * results object throws a NoSuchElementException.
     */
    @Test
    public void getInitializerFromResultsShouldThrowExceptionForUnknownName() throws Exception {
        // Arrange: Create a MultiBackgroundInitializer and initialize it without adding any child initializers.
        // This simulates a scenario where the results object is queried for a name that was never registered.
        MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();
        final String unknownName = "nonExistentInitializer";

        // Act & Assert: Expect a NoSuchElementException when trying to get an initializer with an unknown name.
        try {
            results.getInitializer(unknownName);
            fail("Expected a NoSuchElementException to be thrown for an unknown initializer name.");
        } catch (final NoSuchElementException e) {
            // Verify that the exception message is informative and correct.
            final String expectedMessage = "No child initializer with name " + unknownName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}