package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the {@link DefaultPieDataset} class, focusing on data manipulation methods.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that inserting a value into an empty dataset at index 0 works as expected.
     * The test checks if the item count is updated and if the key and value can be
     * correctly retrieved.
     */
    @Test
    public void testInsertValueIntoEmptyDataset() {
        // Arrange: Create an empty dataset and define the key-value pair to be inserted.
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        final Integer key = 400;
        final Number value = 400;

        // Act: Insert the value at the beginning of the dataset.
        pieDataset.insertValue(0, key, value);

        // Assert: Check that the dataset was updated correctly.
        // 1. The item count should be 1.
        assertEquals("The dataset should contain exactly one item after insertion.", 1, pieDataset.getItemCount());
        // 2. The key at index 0 should be the one we inserted.
        assertEquals("The key at index 0 should be the inserted key.", key, pieDataset.getKey(0));
        // 3. The value retrieved by the key should be the one we inserted.
        assertEquals("The value for the given key should be the inserted value.", value, pieDataset.getValue(key));
    }
}