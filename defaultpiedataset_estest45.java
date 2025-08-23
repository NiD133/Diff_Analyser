package org.jfree.data.general;

import org.junit.Test;

/**
 * Tests for the {@link DefaultPieDataset} class, focusing on exception handling
 * for the {@code insertValue} method.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that insertValue() throws an IllegalArgumentException when the
     * specified position is greater than the dataset's size. An empty dataset
     * can only accept an insert at position 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertValue_shouldThrowException_whenPositionIsOutOfBounds() {
        // Arrange: Create an empty dataset.
        // An empty dataset has a size of 0, so the only valid insert position is 0.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        int invalidPosition = 1;
        String key = "Apples";
        double value = 10.0;

        // Act: Attempt to insert a value at an invalid position (1).
        dataset.insertValue(invalidPosition, key, value);

        // Assert: The test expects an IllegalArgumentException, which is handled
        // by the @Test(expected=...) annotation.
    }

    /**
     * Verifies that insertValue() throws an IllegalArgumentException when the
     * specified position is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertValue_shouldThrowException_whenPositionIsNegative() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        int negativePosition = -1;
        String key = "Oranges";
        double value = 20.0;

        // Act
        dataset.insertValue(negativePosition, key, value);

        // Assert (handled by annotation)
    }
}