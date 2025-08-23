package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that getIndex() returns 0 for the first item added to the dataset.
     * This test also confirms the behavior when the value for the item is null,
     * which is a valid use case.
     */
    @Test
    public void getIndex_shouldReturnZero_forFirstItemAdded() {
        // Arrange: Create an empty dataset and define a key for the new item.
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        final Integer itemKey = 1;

        // Act: Add a single item with a null value and then retrieve its index.
        dataset.setValue(itemKey, null);
        int actualIndex = dataset.getIndex(itemKey);

        // Assert: The index of the first and only item should be 0.
        final int expectedIndex = 0;
        assertEquals("The index of the first item added should be 0", expectedIndex, actualIndex);
    }
}