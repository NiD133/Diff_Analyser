package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DefaultPieDataset} class, focusing on the getIndex method.
 */
public class DefaultPieDatasetGetIndexTest {

    /**
     * Verifies that getIndex() returns the correct zero-based index for a key
     * that exists in the dataset. The index should correspond to the insertion order.
     */
    @Test
    public void getIndex_whenKeyExists_returnsCorrectIndex() {
        // Arrange: Create a dataset and add two key-value pairs.
        // The insertion order determines the index.
        DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>();
        pieDataset.setValue("Slice 1", 10.0); // Expected index: 0
        pieDataset.setValue("Slice 2", 20.0); // Expected index: 1

        // Act: Retrieve the index for the second key that was added.
        String keyToFind = "Slice 2";
        int actualIndex = pieDataset.getIndex(keyToFind);

        // Assert: The returned index should be 1, reflecting the order of insertion.
        int expectedIndex = 1;
        assertEquals("The index for the second added key should be 1.", expectedIndex, actualIndex);
    }
}