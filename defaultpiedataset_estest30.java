package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the equals() method in the DefaultPieDataset class.
 */
public class DefaultPieDataset_ESTestTest30 {

    /**
     * Verifies that the equals() method returns false when comparing two datasets
     * that contain different keys and values.
     */
    @Test
    public void equals_shouldReturnFalse_forDatasetsWithDifferentContent() {
        // Arrange: Create two distinct datasets.
        // The first dataset contains a key with a null value.
        DefaultPieDataset<Integer> dataset1 = new DefaultPieDataset<>();
        dataset1.setValue(100, null);

        // The second dataset contains a different key and a non-null value.
        DefaultPieDataset<Integer> dataset2 = new DefaultPieDataset<>();
        dataset2.setValue(200, 100);

        // Act & Assert: The two datasets have different content and should not be equal.
        assertNotEquals("Datasets with different key-value pairs should not be equal", dataset1, dataset2);
    }
}