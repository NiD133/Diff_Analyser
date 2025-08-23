package org.jfree.data.general;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link DefaultPieDataset} class, focusing on exception handling.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that the remove() method throws an IllegalArgumentException
     * when a null key is provided. This is the expected behavior as null keys
     * are not permitted.
     */
    @Test
    public void remove_withNullKey_shouldThrowIllegalArgumentException() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Act & Assert
        try {
            dataset.remove(null);
            fail("Expected an IllegalArgumentException to be thrown for a null key, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is as expected.
            assertEquals("Null 'key' argument.", e.getMessage());
        }
    }
}