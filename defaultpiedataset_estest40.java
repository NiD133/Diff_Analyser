package org.jfree.data.general;

import org.junit.Test;
import javax.swing.JLayeredPane; // Retained to explain the original constant's value
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the equals() method of the {@link DefaultPieDataset} class.
 */
public class DefaultPieDataset_ESTestTest40 { // Note: Class name kept as per the original file.

    /**
     * Verifies that a DefaultPieDataset created via the copy constructor is equal
     * to the original, especially when the original dataset contains a null value.
     */
    @Test(timeout = 4000)
    public void equals_whenCopiedFromDatasetWithNullValue_shouldReturnTrue() {
        // Arrange: Create an original dataset and add an entry with a null value.
        // The key '400' is used here because the original test used JLayeredPane.DRAG_LAYER, which is an integer constant with the value 400.
        DefaultPieDataset<Integer> originalDataset = new DefaultPieDataset<>();
        Integer key = 400;
        originalDataset.setValue(key, null);

        // Act: Create a new dataset by copying the original one.
        DefaultPieDataset<Integer> copiedDataset = new DefaultPieDataset<>(originalDataset);

        // Assert: The copied dataset should be considered equal to the original.
        assertTrue("A dataset created from a copy should be equal to the original.",
                copiedDataset.equals(originalDataset));
    }
}