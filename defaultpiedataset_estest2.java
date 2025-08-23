package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link DefaultPieDataset} class, focusing on data insertion.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that insertValue() correctly adds a new key-value pair to an
     * empty dataset at the specified position.
     */
    @Test
    public void insertValue_shouldAddKeyValuePair_whenDatasetIsEmpty() {
        // Arrange: Create an empty dataset and define the data to be inserted.
        DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>();
        String key = "Category A";
        double value = 10.5;
        int insertPosition = 0;

        // Act: Insert the new value into the dataset.
        pieDataset.insertValue(insertPosition, key, value);

        // Assert: Verify that the dataset now contains exactly one item
        // with the correct key and value.
        assertEquals("The dataset should contain one item after insertion.", 1, pieDataset.getItemCount());
        assertEquals("The key at the specified position should match the inserted key.", key, pieDataset.getKey(insertPosition));
        assertEquals("The value for the key should match the inserted value.", value, pieDataset.getValue(key).doubleValue(), 0.000001d);
    }
}