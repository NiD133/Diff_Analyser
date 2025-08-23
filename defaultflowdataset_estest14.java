package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link DefaultFlowDataset#setFlow(int, K, K, double)} method,
 * focusing on invalid argument handling.
 */
public class DefaultFlowDataset_ESTestTest14 extends DefaultFlowDataset_ESTest_scaffolding {

    /**
     * Verifies that calling setFlow() with a negative stage index throws an
     * IllegalArgumentException.
     */
    @Test
    public void setFlow_whenStageIsNegative_throwsIllegalArgumentException() {
        // Arrange: Create an empty dataset and define test data with an invalid stage.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        int invalidStage = -1;
        Integer sourceNode = 1;
        Integer destinationNode = 2;
        double flowValue = 10.0;
        
        // For a newly created dataset, the valid stage range is [0, 1].
        String expectedMessage = "Require 'stage' (" + invalidStage + ") to be in the range 0 to 1";

        // Act & Assert: Call the method and verify the exception.
        try {
            dataset.setFlow(invalidStage, sourceNode, destinationNode, flowValue);
            fail("Expected an IllegalArgumentException because the stage is negative.");
        } catch (IllegalArgumentException e) {
            assertEquals("The exception message should match the expected format.",
                    expectedMessage, e.getMessage());
        }
    }
}