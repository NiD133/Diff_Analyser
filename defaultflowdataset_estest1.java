package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A collection of tests for the {@link DefaultFlowDataset} class, focusing on the hashCode() method.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that two equal DefaultFlowDataset instances produce the same hash code,
     * as required by the Object.hashCode() contract.
     */
    @Test
    public void hashCode_shouldReturnSameValueForEqualInstances() {
        // Arrange: Create two identical datasets, each with a single flow.
        // Using descriptive variables for nodes makes the setup clear.
        String sourceNode = "Source A";
        String destinationNode = "Destination B";
        double flowValue = 100.0;
        int stage = 0;

        DefaultFlowDataset<String> dataset1 = new DefaultFlowDataset<>();
        dataset1.setFlow(stage, sourceNode, destinationNode, flowValue);

        DefaultFlowDataset<String> dataset2 = new DefaultFlowDataset<>();
        dataset2.setFlow(stage, sourceNode, destinationNode, flowValue);

        // Act & Assert: The datasets should be considered equal, and therefore
        // their hash codes must also be equal.
        assertTrue("Datasets with identical content should be equal.", dataset1.equals(dataset2));
        assertEquals("Equal datasets must have the same hash code.",
                     dataset1.hashCode(), dataset2.hashCode());
    }
}