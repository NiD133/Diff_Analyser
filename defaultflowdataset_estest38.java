package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class provides an improved version of a generated test case
 * for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDataset_ESTestTest38 {

    /**
     * Verifies that calling {@code setNodeProperty} for an existing property
     * key overwrites the previous value.
     *
     * The original test for this behavior was difficult to understand due to
     * meaningless variable names and obscure constants (e.g., from Swing).
     * More importantly, its assertion was incorrect, as it tested a property
     * of the input key rather than the state of the dataset after the method
     * call. This revised test corrects these issues.
     */
    @Test
    public void setNodeProperty_whenCalledTwiceForSameKey_overwritesValue() {
        // Arrange: Create a dataset and define a node, a property key,
        // and two distinct values for that property.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        NodeKey<Integer> nodeKey = new NodeKey<>(0, 100); // A simple, representative node key
        String propertyKey = "name";
        String initialValue = "Initial Node Name";
        String updatedValue = "Updated Node Name";

        // Act: Set the property twice with the two different values.
        dataset.setNodeProperty(nodeKey, propertyKey, initialValue);
        dataset.setNodeProperty(nodeKey, propertyKey, updatedValue);

        // Assert: Verify that the value retrieved from the dataset is the
        // second (updated) value, confirming the overwrite behavior.
        Object retrievedValue = dataset.getNodeProperty(nodeKey, propertyKey);
        assertEquals("The property value should have been overwritten by the second call.",
                updatedValue, retrievedValue);
    }
}