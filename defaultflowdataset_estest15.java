package org.jfree.data.flow;

import org.junit.Test;

/**
 * Unit tests for the {@link DefaultFlowDataset} class, focusing on edge cases and
 * exception handling.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that calling getSources() with an invalid stage index throws an
     * IndexOutOfBoundsException on an empty dataset.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getSourcesShouldThrowExceptionForInvalidStageIndex() {
        // Arrange: Create a new, empty dataset.
        // A new DefaultFlowDataset is initialized with one stage (index 0),
        // meaning its stage count is 1.
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        
        // The only valid stage index is 0. We will test with the first invalid index.
        int invalidStageIndex = 1;

        // Act & Assert: Attempting to access sources for a non-existent stage
        // should throw an IndexOutOfBoundsException. The assertion is handled
        // by the 'expected' attribute of the @Test annotation.
        dataset.getSources(invalidStageIndex);
    }
}