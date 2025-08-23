package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that getIndex() returns -1 for a key that does not exist in the dataset.
     * This also implicitly tests the behavior for an empty dataset.
     */
    @Test
    public void getIndex_forNonExistentKey_shouldReturnNegativeOne() {
        // Arrange: Create an empty pie dataset.
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        Integer nonExistentKey = 400; // An arbitrary key not present in the dataset.

        // Act: Attempt to get the index of the non-existent key.
        int actualIndex = dataset.getIndex(nonExistentKey);

        // Assert: The returned index should be -1, indicating the key was not found.
        assertEquals(-1, actualIndex);
    }
}