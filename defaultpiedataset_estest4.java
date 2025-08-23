package org.jfree.data.general;

import org.jfree.chart.api.SortOrder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the DefaultPieDataset class.
 * The original class name and inheritance structure from the EvoSuite-generated
 * test have been preserved.
 */
public class DefaultPieDataset_ESTestTest4 extends DefaultPieDataset_ESTest_scaffolding {

    /**
     * Verifies that calling sortByKeys() on an empty dataset does not cause an
     * error and leaves the dataset in its empty state. This is an important
     * edge case to ensure robustness.
     */
    @Test
    public void sortByKeysOnEmptyDatasetShouldDoNothing() {
        // Arrange: Create an empty pie dataset.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Act: Attempt to sort the empty dataset by its keys.
        dataset.sortByKeys(SortOrder.DESCENDING);

        // Assert: The dataset should remain empty after the sort operation.
        assertEquals("The item count should still be 0 after sorting an empty dataset.", 0, dataset.getItemCount());
    }
}