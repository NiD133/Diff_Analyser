package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable, behavior-oriented tests for DefaultFlowDataset.
 *
 * Naming convention:
 * - Method names describe behavior under test and expected result.
 * - Assertions include messages to clarify failure causes.
 */
public class DefaultFlowDatasetTest {

    private static final int STAGE_0 = 0;
    private static final int STAGE_1 = 1;

    private static final String SRC_A = "A";
    private static final String DST_Z = "Z";
    private static final String DST_Y = "Y";
    private static final String DST_P = "P";

    @Test
    @DisplayName("getFlow returns the value that was previously set for a stage/source/destination")
    public void getFlow_returnsPreviouslySetValue() {
        // Arrange
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();

        // Act
        dataset.setFlow(STAGE_0, SRC_A, DST_Z, 1.5);

        // Assert
        assertEquals(1.5, dataset.getFlow(STAGE_0, SRC_A, DST_Z),
                "Expected to read back the exact flow value that was set");
    }

    @Test
    @DisplayName("getStageCount reflects the number of stages present in the dataset")
    public void stageCount_reflectsNumberOfStages() {
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();

        // An empty dataset still has one stage by definition
        assertEquals(1, dataset.getStageCount(),
                "Empty dataset should report a single stage");

        // Adding a flow at stage 0 keeps stage count at 1
        dataset.setFlow(STAGE_0, SRC_A, DST_Z, 11.1);
        assertEquals(1, dataset.getStageCount(),
                "Adding a flow at stage 0 should not change stage count");

        // Adding a flow at a new stage (stage 1) increases stage count to 2
        dataset.setFlow(STAGE_1, DST_Z, DST_P, 5.0);
        assertEquals(2, dataset.getStageCount(),
                "Adding a flow at a new stage should increase the stage count");
    }

    @Test
    @DisplayName("equals distinguishes datasets based on their flows")
    public void equals_comparesFlows() {
        DefaultFlowDataset<String> d1 = new DefaultFlowDataset<>();
        DefaultFlowDataset<String> d2 = new DefaultFlowDataset<>();

        assertEquals(d1, d2, "Two new datasets should be equal");

        d1.setFlow(STAGE_0, SRC_A, DST_Z, 1.0);
        assertNotEquals(d1, d2,
                "Datasets should differ after adding a flow to only one of them");

        d2.setFlow(STAGE_0, SRC_A, DST_Z, 1.0);
        assertEquals(d1, d2,
                "Datasets should be equal again after adding the same flow to both");
    }

    @Test
    @DisplayName("Serialization round-trip preserves equality")
    public void serialization_roundTripPreservesEquality() {
        DefaultFlowDataset<String> original = new DefaultFlowDataset<>();
        original.setFlow(STAGE_0, SRC_A, DST_Z, 1.0);

        DefaultFlowDataset<String> restored = TestUtils.serialised(original);
        assertEquals(original, restored, "Serialized then deserialized dataset should be equal");
    }

    @Test
    @DisplayName("clone creates a deep copy; modifying the original does not affect the clone")
    public void clone_createsDeepCopy() throws CloneNotSupportedException {
        DefaultFlowDataset<String> original = new DefaultFlowDataset<>();
        original.setFlow(STAGE_0, SRC_A, DST_Z, 1.0);

        DefaultFlowDataset<String> clone = (DefaultFlowDataset<String>) original.clone();

        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should have the same runtime class");
        assertEquals(original, clone, "Clone should be equal to the original initially");

        // Modify the original; the clone should remain unchanged (deep copy)
        original.setFlow(STAGE_0, SRC_A, DST_Y, 8.0);
        assertNotEquals(original, clone, "Modifying original should not change the clone");

        // Bring the clone to the same state to re-establish equality
        clone.setFlow(STAGE_0, SRC_A, DST_Y, 8.0);
        assertEquals(original, clone, "After matching updates, datasets should be equal again");
    }

    @Test
    @DisplayName("Class implements PublicCloneable")
    public void implementsPublicCloneable() {
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        assertTrue(dataset instanceof PublicCloneable,
                "Dataset should implement PublicCloneable");
    }
}