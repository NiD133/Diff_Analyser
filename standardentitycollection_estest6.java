package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link StandardEntityCollection} class, focusing on exception handling.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that the add() method throws an IllegalArgumentException
     * when a null entity is passed as an argument. This is the expected behavior
     * as collections should not contain null entities.
     */
    @Test
    public void add_whenAddingNull_shouldThrowIllegalArgumentException() {
        // Arrange: Create an empty entity collection.
        StandardEntityCollection collection = new StandardEntityCollection();
        String expectedErrorMessage = "Null 'entity' argument.";

        // Act & Assert: Attempt to add a null entity and verify the exception.
        try {
            collection.add(null);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}