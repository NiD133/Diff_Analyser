package org.jfree.data.general;

import org.jfree.chart.date.SpreadsheetDate;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test class focuses on the equals() method of the DefaultKeyedValueDataset class.
 * The original test class name, DefaultKeyedValueDataset_ESTestTest13, is kept for context.
 */
public class DefaultKeyedValueDataset_ESTestTest13 extends DefaultKeyedValueDataset_ESTest_scaffolding {

    /**
     * Tests that the equals() method returns false for two datasets
     * that share the same key but have different values.
     */
    @Test
    public void equals_withSameKeyAndDifferentValue_shouldReturnFalse() {
        // Arrange: Create two datasets with the same key but different numerical values.
        SpreadsheetDate commonKey = new SpreadsheetDate(379);
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset(commonKey, 2);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset(commonKey, 3);

        // Act: Compare the two datasets for equality.
        boolean areEqual = dataset1.equals(dataset2);

        // Assert: The datasets should not be considered equal.
        assertFalse("Datasets with the same key but different values should not be equal.", areEqual);
    }
}