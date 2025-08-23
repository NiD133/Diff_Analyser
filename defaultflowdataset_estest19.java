package org.jfree.data.flow;

import org.junit.Test;

/**
 * Tests for the {@link DefaultFlowDataset} class, focusing on boundary conditions
 * and exception handling.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that calling getDestinations() with an invalid stage index
     * throws an IndexOutOfBoundsException.
     * <p>
     * According to the class design, a newly created DefaultFlowDataset has one
     * stage, which is at index 0. Therefore, attempting to access stage index 1
     * should fail.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getDestinations_withInvalidStageIndex_shouldThrowException() {
        // Arrange: Create a new, empty dataset.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();

        // Act: Attempt to get destinations for a stage index that is out of bounds.
        // An empty dataset has one stage (index 0), so index 1 is invalid.
        dataset.getDestinations(1);

        // Assert: An IndexOutOfBoundsException is expected, which is handled by the
        // @Test(expected=...) annotation.
    }
}