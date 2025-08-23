package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Contains tests for the equals() method of the {@link DefaultFlowDataset} class,
 * particularly focusing on its interaction with cloning.
 */
public class DefaultFlowDatasetEqualsTest {

    /**
     * Verifies that a dataset is no longer equal to its clone after the
     * original dataset has been modified.
     */
    @Test
    public void testEquals_ReturnsFalse_WhenOriginalDatasetIsModifiedAfterCloning() throws CloneNotSupportedException {
        // Arrange: Create a dataset, add an initial flow, and then clone it.
        DefaultFlowDataset<Integer> originalDataset = new DefaultFlowDataset<>();
        Integer nodeA = 100;
        Integer nodeB = 200;
        originalDataset.setFlow(0, nodeA, nodeA, 10.0);

        DefaultFlowDataset<Integer> clonedDataset = (DefaultFlowDataset<Integer>) originalDataset.clone();

        // Sanity check: Ensure the dataset and its fresh clone are equal.
        assertEquals("A dataset and its clone should be equal immediately after cloning.",
                originalDataset, clonedDataset);

        // Act: Modify the original dataset by adding a new flow. This should not
        // affect the clone.
        originalDataset.setFlow(0, nodeA, nodeB, 50.0);

        // Assert: The modified original dataset should no longer be equal to the
        // unchanged clone.
        assertNotEquals("Modifying the original dataset should make it unequal to its clone.",
                originalDataset, clonedDataset);

        // Verify that the stage counts remain consistent as expected.
        assertEquals("Stage count of original dataset should be 1.", 1, originalDataset.getStageCount());
        assertEquals("Stage count of cloned dataset should remain 1.", 1, clonedDataset.getStageCount());
    }
}