package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains tests for the DefaultPieDataset class.
 * This particular test focuses on the behavior of the equals() method.
 */
public class DefaultPieDataset_ESTestTest28 extends DefaultPieDataset_ESTest_scaffolding {

    /**
     * Tests that the equals() method returns false when two datasets,
     * initially identical, are made different by changing a value in one of them.
     */
    @Test
    public void equals_shouldReturnFalse_whenValuesForSameKeyDiffer() {
        // Arrange: Create a dataset with a key mapped to a null value.
        DefaultPieDataset<Integer> dataset1 = new DefaultPieDataset<>();
        Integer testKey = 1;
        dataset1.setValue(testKey, null);

        // Create a second dataset as a copy of the first.
        DefaultPieDataset<Integer> dataset2 = new DefaultPieDataset<>(dataset1);

        // Sanity check: The two datasets should be equal immediately after copying.
        assertEquals("Datasets should be equal after one is copied from the other.", dataset1, dataset2);
        assertEquals("Hash codes should be equal for equal objects.", dataset1.hashCode(), dataset2.hashCode());

        // Act: Modify the value for the key in the second dataset, changing it from null to a number.
        dataset2.setValue(testKey, 400);

        // Assert: The datasets should no longer be equal after the modification.
        assertNotEquals("Datasets should not be equal after a value is modified in one.", dataset1, dataset2);
    }
}