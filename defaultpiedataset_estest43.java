package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the exception-throwing behavior of the {@link DefaultPieDataset#insertValue} method.
 */
public class DefaultPieDatasetInsertTest {

    @Test
    public void insertValue_withOutOfBoundsPosition_shouldThrowIllegalArgumentException() {
        // Arrange: Create an empty dataset. For an empty dataset, the only valid
        // insertion position is 0. Any other position is out of bounds.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        final int invalidPosition = 1;
        final String key = "Any Key";
        final double value = 10.0;

        // Act & Assert
        try {
            dataset.insertValue(invalidPosition, key, value);
            fail("Expected an IllegalArgumentException because the insertion position is out of bounds.");
        } catch (IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("'position' out of bounds.", e.getMessage());
        }
    }
}