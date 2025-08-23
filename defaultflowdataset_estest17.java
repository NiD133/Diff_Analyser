package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link DefaultFlowDataset} class, focusing on argument validation.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that the getInFlows() method throws an IllegalArgumentException
     * when a null nodeKey is provided. This ensures robust handling of invalid input.
     */
    @Test
    public void getInFlowsShouldThrowExceptionForNullNodeKey() {
        // Arrange: Create an instance of the dataset.
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();

        // Act & Assert: Expect an IllegalArgumentException when calling the method with a null argument.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dataset.getInFlows(null);
        });

        // Assert: Verify that the exception message is correct, confirming the reason for the failure.
        assertEquals("Null 'nodeKey' argument.", exception.getMessage());
    }
}