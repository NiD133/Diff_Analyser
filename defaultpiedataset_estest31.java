package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite focuses on the equals() method of the DefaultPieDataset class.
 */
public class DefaultPieDataset_ESTestTest31 {

    /**
     * Tests that the equals() method correctly identifies two datasets as unequal
     * after one of them has been modified.
     * It follows these steps:
     * 1. Creates two empty DefaultPieDataset instances, which should be equal.
     * 2. Adds a key-value pair to the first dataset.
     * 3. Asserts that the two datasets are no longer equal.
     */
    @Test
    public void equals_shouldReturnFalse_whenDatasetsHaveDifferentContent() {
        // Arrange: Create two datasets.
        DefaultPieDataset<String> dataset1 = new DefaultPieDataset<>();
        DefaultPieDataset<String> dataset2 = new DefaultPieDataset<>();

        // Sanity check: Two empty datasets should be equal.
        assertTrue("Two newly created, empty datasets should be equal", dataset1.equals(dataset2));

        // Act: Modify one of the datasets by adding a value.
        dataset1.setValue("Apples", 50.0);

        // Assert: The modified dataset should no longer be equal to the empty one.
        assertFalse("A dataset with data should not be equal to an empty dataset", dataset1.equals(dataset2));
    }
}