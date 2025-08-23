package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link DefaultFlowDataset} class, focusing on cloning and equality.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that cloning a dataset results in a new dataset that is equal
     * to the original.
     */
    @Test
    public void cloneShouldProduceEqualInstance() throws CloneNotSupportedException {
        // Arrange: Create a dataset and add a single flow to it.
        DefaultFlowDataset<Integer> originalDataset = new DefaultFlowDataset<>();
        final int stage = 0;
        final Integer sourceNode = 100;
        final Integer destinationNode = 200;
        final double flowValue = 50.0;
        
        originalDataset.setFlow(stage, sourceNode, destinationNode, flowValue);

        // Act: Clone the original dataset.
        DefaultFlowDataset<Integer> clonedDataset = (DefaultFlowDataset<Integer>) originalDataset.clone();

        // Assert: The cloned dataset should be equal to the original and contain the same data.
        assertEquals("The cloned dataset should be equal to the original.", originalDataset, clonedDataset);
        assertTrue("The cloned dataset should not be the same instance as the original.", originalDataset != clonedDataset);
        
        // Verify key properties of the clone to ensure it's a faithful copy.
        assertEquals("Cloned dataset should have the same stage count.", 1, clonedDataset.getStageCount());
        assertEquals("Cloned dataset should contain the correct flow value.", 
                     flowValue, clonedDataset.getFlow(stage, sourceNode, destinationNode).doubleValue(), 0.001);
    }
}