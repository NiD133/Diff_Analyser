package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import org.junit.Test;

/**
 * Test suite for {@link MultiBackgroundInitializer}.
 * This specific test focuses on the behavior of the MultiBackgroundInitializerResults class.
 */
// Note: The original test class name and inheritance are kept to match the provided context.
public class MultiBackgroundInitializer_ESTestTest11 extends MultiBackgroundInitializer_ESTest_scaffolding {

    /**
     * Verifies that calling getException() with a null name on the results object
     * throws a NoSuchElementException.
     */
    @Test
    public void getExceptionShouldThrowNoSuchElementExceptionWhenNameIsNull() throws Exception {
        // Arrange
        final MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        // Add a dummy child initializer; this is required to get a valid results object.
        multiInitializer.addInitializer("childInitializer", new BackgroundInitializer<>());

        // Act
        // The initialize() method must be called to produce the results object.
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Assert
        try {
            results.getException(null);
            fail("Expected a NoSuchElementException to be thrown for a null initializer name.");
        } catch (final NoSuchElementException e) {
            // Verify that the correct exception with the expected message is thrown.
            assertEquals("No child initializer with name null", e.getMessage());
        }
    }
}