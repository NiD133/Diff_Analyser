package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link DefaultFlowDataset} class, focusing on exception handling.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that the getOutFlows() method throws an IllegalArgumentException
     * when the provided nodeKey is null. This is the expected behavior for
     * methods that do not permit null arguments.
     */
    @Test
    public void getOutFlows_withNullNodeKey_throwsIllegalArgumentException() {
        // Arrange: Create an empty dataset instance.
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();

        // Act & Assert: Call the method with a null argument and verify the exception.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> dataset.getOutFlows(null)
        );

        // Assert: Check if the exception message is as expected.
        assertEquals("Null 'nodeKey' argument.", exception.getMessage());
    }
}