package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains tests for the DefaultPieDataset class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class DefaultPieDataset_ESTestTest17 extends DefaultPieDataset_ESTest_scaffolding {

    /**
     * Tests that calling insertValue() with an index greater than the dataset's size
     * throws an IndexOutOfBoundsException.
     *
     * Note: The original auto-generated test used 'DefaultKeyedValuesDataset'.
     * It has been corrected to test 'DefaultPieDataset' as intended.
     */
    @Test
    public void insertValue_atInvalidIndex_throwsIndexOutOfBoundsException() {
        // Arrange: Create a dataset and add one item, making its size 1.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Apple", 10);
        assertEquals("Dataset should have one item after setup.", 1, dataset.getItemCount());

        // The valid indices for insertion are 0 (to prepend) and 1 (to append).
        // An index of 2 is therefore out of bounds.
        int invalidIndex = 2;
        String newKey = "Orange";
        double newValue = 20;

        // Act & Assert: Attempt to insert a new value at the invalid index.
        try {
            dataset.insertValue(invalidIndex, newKey, newValue);
            fail("Expected an IndexOutOfBoundsException because the insertion index is out of bounds.");
        } catch (IndexOutOfBoundsException e) {
            // This is the expected outcome.
            // For robustness, we can verify the exception message.
            String actualMessage = e.getMessage();
            assertTrue("Exception message should contain the invalid index.",
                    actualMessage.contains("Index: " + invalidIndex));
            assertTrue("Exception message should contain the dataset size.",
                    actualMessage.contains("Size: " + dataset.getItemCount()));
        }
    }
}