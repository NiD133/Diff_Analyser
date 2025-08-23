package org.jfree.data.general;

import org.jfree.data.DefaultKeyedValuesDataset;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite contains tests for the DefaultPieDataset class, focusing on
 * its equals() method behavior.
 */
public class DefaultPieDataset_ESTestTest27 {

    /**
     * Verifies that the equals() method returns false after a DefaultPieDataset,
     * created from a source, is modified. This ensures that the comparison is
     * based on the dataset's current values.
     */
    @Test
    public void equals_returnsFalse_afterModifyingDatasetCreatedFromSource() {
        // Arrange: Create a source dataset and two identical PieDatasets from it.
        DefaultKeyedValuesDataset<String> sourceDataset = new DefaultKeyedValuesDataset<>();
        sourceDataset.setValue("Apples", 50.0);

        // Create the dataset that we will subsequently modify.
        DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>(sourceDataset);
        
        // Create a second dataset from the same source to represent the original state for comparison.
        DefaultPieDataset<String> originalStatePieDataset = new DefaultPieDataset<>(sourceDataset);

        // Sanity check: The two pie datasets should be equal immediately after creation.
        assertTrue("Datasets created from the same source should be equal initially.", 
                   pieDataset.equals(originalStatePieDataset));

        // Act: Modify the value for the "Apples" key in the first pie dataset.
        pieDataset.setValue("Apples", 75.0);

        // Assert: The modified dataset should no longer be equal to the dataset
        // representing the original state.
        assertFalse("A modified dataset should not be equal to its original state.", 
                    pieDataset.equals(originalStatePieDataset));
    }
}