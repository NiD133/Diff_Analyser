package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class focuses on the hashCode() method of the DefaultPieDataset.
 * The original test was auto-generated and lacked clarity and assertions.
 * This version provides a meaningful test for the hashCode() contract.
 */
public class DefaultPieDataset_ESTestTest36 { // Retaining original class name as per instructions.

    /**
     * Tests that two separate but equal (empty) DefaultPieDataset instances
     * produce the same hash code, fulfilling the Object.hashCode() contract.
     * The contract states that if two objects are equal according to the equals()
     * method, then calling the hashCode() method on each of the two objects
     * must produce the same integer result.
     */
    @Test
    public void hashCode_shouldBeEqualForTwoEmptyDatasets() {
        // Arrange: Create two separate, empty DefaultPieDataset instances.
        // These should be considered equal to each other.
        DefaultPieDataset<String> dataset1 = new DefaultPieDataset<>();
        DefaultPieDataset<String> dataset2 = new DefaultPieDataset<>();

        // Assert: First, confirm that the two empty datasets are indeed equal.
        assertEquals("Two new empty datasets should be considered equal.", dataset1, dataset2);

        // Act & Assert: Now, verify that their hash codes are also equal,
        // which is required by the hashCode() contract.
        assertEquals("Hash codes for two equal empty datasets must be the same.",
                dataset1.hashCode(), dataset2.hashCode());
    }
}