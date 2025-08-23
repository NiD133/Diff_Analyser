package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Tests the {@link DefaultFlowDataset#getFlow(int, Object, Object)} method.
     */
    @Test
    public void testGetFlow() {
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(0, "A", "Z", 1.5);
        assertEquals(1.5, dataset.getFlow(0, "A", "Z"), 
            "The flow value should be 1.5 for the given stage and nodes.");
    }

    /**
     * Tests the {@link DefaultFlowDataset#getStageCount()} method.
     */
    @Test
    public void testGetStageCount() {
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        assertEquals(1, dataset.getStageCount(), 
            "Initially, the stage count should be 1.");

        dataset.setFlow(0, "A", "Z", 11.1);
        assertEquals(1, dataset.getStageCount(), 
            "Adding a flow to an existing stage should not change the stage count.");

        dataset.setFlow(1, "Z", "P", 5.0);
        assertEquals(2, dataset.getStageCount(), 
            "Adding a flow to a new stage should increase the stage count.");
    }

    /**
     * Tests the {@link DefaultFlowDataset#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        DefaultFlowDataset<String> dataset1 = new DefaultFlowDataset<>();
        DefaultFlowDataset<String> dataset2 = new DefaultFlowDataset<>();
        assertEquals(dataset1, dataset2, 
            "Two empty datasets should be equal.");

        dataset1.setFlow(0, "A", "Z", 1.0);
        assertNotEquals(dataset1, dataset2, 
            "Datasets with different flows should not be equal.");

        dataset2.setFlow(0, "A", "Z", 1.0);
        assertEquals(dataset1, dataset2, 
            "Datasets with identical flows should be equal.");
    }

    /**
     * Tests the serialization and deserialization of a {@link DefaultFlowDataset}.
     */
    @Test
    public void testSerialization() {
        DefaultFlowDataset<String> originalDataset = new DefaultFlowDataset<>();
        originalDataset.setFlow(0, "A", "Z", 1.0);
        DefaultFlowDataset<String> deserializedDataset = TestUtils.serialised(originalDataset);
        assertEquals(originalDataset, deserializedDataset, 
            "The deserialized dataset should be equal to the original.");
    }

    /**
     * Tests the cloning functionality of {@link DefaultFlowDataset}.
     * @throws CloneNotSupportedException if cloning is not supported.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        DefaultFlowDataset<String> originalDataset = new DefaultFlowDataset<>();
        originalDataset.setFlow(0, "A", "Z", 1.0);
        DefaultFlowDataset<String> clonedDataset = (DefaultFlowDataset<String>) originalDataset.clone();

        assertNotSame(originalDataset, clonedDataset, 
            "The cloned dataset should be a different instance.");
        assertEquals(originalDataset, clonedDataset, 
            "The cloned dataset should be equal to the original.");

        originalDataset.setFlow(0, "A", "Y", 8.0);
        assertNotEquals(originalDataset, clonedDataset, 
            "Modifying the original dataset should not affect the clone.");
    }

    /**
     * Tests that {@link DefaultFlowDataset} implements {@link PublicCloneable}.
     */
    @Test
    public void testPublicCloneable() {
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        assertTrue(dataset instanceof PublicCloneable, 
            "DefaultFlowDataset should implement PublicCloneable.");
    }
}