package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the equals() and clone() methods of the DefaultFlowDataset class.
 */
public class DefaultFlowDatasetEqualityTest {

    @Test
    public void equals_returnsFalseWhenComparingToACloneOfADifferentDataset() throws CloneNotSupportedException {
        // Arrange: Create two distinct but initially identical datasets.
        DefaultFlowDataset<Integer> originalDataset = new DefaultFlowDataset<>();
        DefaultFlowDataset<Integer> datasetToModify = new DefaultFlowDataset<>();

        // Sanity check: ensure they are equal when empty.
        assertTrue("Two new empty datasets should be equal", originalDataset.equals(datasetToModify));

        // Act: Modify one dataset and then create a clone of it.
        Integer node = 0;
        double flowValue = 1.0;
        datasetToModify.setFlow(1, node, node, flowValue);
        
        Object cloneOfModifiedDataset = datasetToModify.clone();

        // Assert: The original empty dataset should not be equal to the modified one or its clone.
        assertFalse("An empty dataset should not be equal to a modified one",
                originalDataset.equals(datasetToModify));
        assertFalse("An empty dataset should not be equal to the clone of a modified one",
                originalDataset.equals(cloneOfModifiedDataset));

        // Also, verify that the modified dataset is equal to its own clone.
        assertTrue("A dataset should be equal to its clone",
                datasetToModify.equals(cloneOfModifiedDataset));
    }
}