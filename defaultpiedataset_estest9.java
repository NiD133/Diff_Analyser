package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link DefaultPieDataset} class.
 * This class focuses on verifying the behavior of the getItemCount() method.
 */
// The original test class name 'DefaultPieDataset_ESTestTest9' and its scaffolding
// are kept to show the direct improvement of the provided code.
public class DefaultPieDataset_ESTestTest9 extends DefaultPieDataset_ESTest_scaffolding {

    /**
     * Verifies that getItemCount() returns 1 after a single item has been added
     * to an empty dataset.
     */
    @Test
    public void getItemCount_afterAddingOneItem_returnsOne() {
        // Arrange: Create an empty dataset.
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        String testKey = "Firefox";
        double testValue = 45.0;

        // Act: Add a single key-value pair to the dataset.
        dataset.setValue(testKey, testValue);
        int itemCount = dataset.getItemCount();

        // Assert: The item count should now be 1.
        assertEquals("The item count should be 1 after adding a single item.", 1, itemCount);
    }
}