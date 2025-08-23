package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test case evaluates the behavior of the insertValue method in the DefaultPieDataset class.
 */
public class DefaultPieDataset_ESTestTest6 extends DefaultPieDataset_ESTest_scaffolding {

    /**
     * Tests that inserting a value at the beginning of an empty dataset correctly
     * adds the new element and results in an item count of 1.
     */
    @Test(timeout = 4000)
    public void testInsertValueAtStartOfEmptyDataset() {
        // Arrange: Create an empty dataset and define the data to be inserted.
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        Integer key = 1;
        double value = 50.0;
        int insertPosition = 0;

        // Act: Insert the new value at the specified position (index 0).
        dataset.insertValue(insertPosition, key, value);

        // Assert: Verify that the dataset now contains exactly one item,
        // and that the key and value at index 0 are correct.
        assertEquals("After inserting one item, the item count should be 1.", 1, dataset.getItemCount());
        assertEquals("The key at index 0 should match the inserted key.", key, dataset.getKey(0));
        assertEquals("The value at index 0 should match the inserted value.", value, dataset.getValue(0).doubleValue(), 0.0d);
    }
}