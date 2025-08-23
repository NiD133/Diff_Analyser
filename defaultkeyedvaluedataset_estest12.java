package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the equals() method in the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Verifies that two empty datasets are considered equal, especially when
     * one is constructed by wrapping the other.
     */
    @Test
    public void equals_shouldReturnTrueForTwoLogicallyEmptyDatasets() {
        // Arrange: Create two datasets that are both logically empty.
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset(dataset1);

        // Assert: The two datasets should be considered equal.
        // This also verifies that their hash codes are equal, as required by the
        // equals() and hashCode() contract.
        assertEquals(dataset1, dataset2);
        assertEquals("Hash codes of equal objects must be equal.", 
                     dataset1.hashCode(), dataset2.hashCode());
    }
}