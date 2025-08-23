package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    /**
     * Tests the {@code equals} method to ensure it correctly identifies equal and unequal datasets.
     */
    @Test
    public void testEqualsMethod() {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Test", 45.5);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Test", 45.5);

        // Test equality for identical datasets
        assertEquals(dataset1, dataset2);
        assertEquals(dataset2, dataset1);

        // Test inequality for datasets with different keys
        dataset1 = new DefaultKeyedValueDataset("Test 1", 45.5);
        dataset2 = new DefaultKeyedValueDataset("Test 2", 45.5);
        assertNotEquals(dataset1, dataset2);

        // Test inequality for datasets with different values
        dataset1 = new DefaultKeyedValueDataset("Test", 45.5);
        dataset2 = new DefaultKeyedValueDataset("Test", 45.6);
        assertNotEquals(dataset1, dataset2);
    }

    /**
     * Tests the cloning functionality to ensure a dataset can be cloned correctly.
     * @throws CloneNotSupportedException if the dataset cannot be cloned.
     */
    @Test
    public void testCloningFunctionality() throws CloneNotSupportedException {
        DefaultKeyedValueDataset originalDataset = new DefaultKeyedValueDataset("Test", 45.5);
        DefaultKeyedValueDataset clonedDataset = (DefaultKeyedValueDataset) originalDataset.clone();

        // Ensure the cloned dataset is a different instance but equal in content
        assertNotSame(originalDataset, clonedDataset);
        assertSame(originalDataset.getClass(), clonedDataset.getClass());
        assertEquals(originalDataset, clonedDataset);
    }

    /**
     * Tests that a cloned dataset is independent of the original dataset.
     * @throws CloneNotSupportedException if the dataset cannot be cloned.
     */
    @Test
    public void testCloneIndependence() throws CloneNotSupportedException {
        DefaultKeyedValueDataset originalDataset = new DefaultKeyedValueDataset("Key", 10.0);
        DefaultKeyedValueDataset clonedDataset = CloneUtils.clone(originalDataset);

        // Ensure the cloned dataset is initially equal to the original
        assertEquals(originalDataset, clonedDataset);

        // Modify the cloned dataset and ensure it is no longer equal to the original
        clonedDataset.updateValue(99.9);
        assertNotEquals(originalDataset, clonedDataset);

        // Revert the change and ensure equality is restored
        clonedDataset.updateValue(10.0);
        assertEquals(originalDataset, clonedDataset);
    }

    /**
     * Tests the serialization and deserialization process to ensure dataset integrity.
     */
    @Test
    public void testSerializationIntegrity() {
        DefaultKeyedValueDataset originalDataset = new DefaultKeyedValueDataset("Test", 25.3);
        DefaultKeyedValueDataset deserializedDataset = TestUtils.serialised(originalDataset);

        // Ensure the deserialized dataset is equal to the original
        assertEquals(originalDataset, deserializedDataset);
    }
}