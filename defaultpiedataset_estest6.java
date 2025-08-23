package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that inserting a value into an empty dataset at the first position
     * correctly adds the new item and results in an item count of one.
     */
    @Test
    public void insertValue_intoEmptyDataset_addsItemAndIncreasesCount() {
        // Arrange: Create an empty dataset and define the data to be inserted.
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        Integer key = 1;
        double value = 100.0;
        int insertPosition = 0;

        // Act: Insert the new value into the dataset.
        dataset.insertValue(insertPosition, key, value);

        // Assert: Verify that the dataset's state is correct after the insertion.
        assertEquals("The item count should be 1 after inserting one value.", 1, dataset.getItemCount());
        assertEquals("The key at the insertion index should match the inserted key.", key, dataset.getKey(0));
        assertEquals("The value at the insertion index should match the inserted value.", value, dataset.getValue(0).doubleValue(), 0.0);
    }
}