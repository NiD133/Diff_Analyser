package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An instance of DefaultFlowDataset should always be equal to itself.
     */
    @Test
    public void equals_onSameInstance_shouldReturnTrue() {
        // Arrange
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();

        // Act & Assert
        // According to the contract of Object.equals(), an object must be equal to itself.
        assertEquals(dataset, dataset);
    }
}