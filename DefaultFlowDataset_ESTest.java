package org.jfree.data.flow;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for DefaultFlowDataset.
 *
 * Conventions used:
 * - Nodes are simple strings ("A", "B", "C") to keep intent clear.
 * - Each test follows Arrange / Act / Assert structure.
 * - Assertions include a brief message to explain the expected behavior.
 */
public class DefaultFlowDatasetTest {

    // ------------------------------------------------------------------------
    // Basics
    // ------------------------------------------------------------------------

    @Test
    public void initialStageCount_isOne() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();

        assertEquals("An empty dataset should report 1 stage", 1, ds.getStageCount());
    }

    @Test
    public void sourcesAndDestinations_areEmptyInitially() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();

        assertTrue("No sources at stage 0 initially", ds.getSources(0).isEmpty());
        assertTrue("No destinations at stage 0 initially", ds.getDestinations(0).isEmpty());
    }

    // ------------------------------------------------------------------------
    // Adding flows
    // ------------------------------------------------------------------------

    @Test
    public void setFlow_addsNodesAndCanBeQueried() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();

        // Arrange
        ds.setFlow(0, "A", "B", 3.5);

        // Act
        Number flow = ds.getFlow(0, "A", "B");
        List<String> sources = ds.getSources(0);
        List<String> destinations = ds.getDestinations(0);

        // Assert
        assertEquals("Flow value should be retrievable", 3.5, flow.doubleValue(), 0.0000001);
        assertTrue("Source list should contain 'A'", sources.contains("A"));
        assertTrue("Destination list should contain 'B'", destinations.contains("B"));
        assertEquals("Stage count remains 1 when writing to stage 0", 1, ds.getStageCount());
    }

    @Test
    public void setFlow_atNextStage_extendsStageCount() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();

        // Arrange / Act
        ds.setFlow(1, "B", "C", 2.0);

        // Assert
        assertEquals("Writing to stage 1 should extend stage count to 2", 2, ds.getStageCount());
        assertTrue("Sources at stage 1 should include 'B'", ds.getSources(1).contains("B"));
        assertTrue("Destinations at stage 1 should include 'C'", ds.getDestinations(1).contains("C"));
    }

    @Test
    public void getFlow_returnsNullWhenNoFlowDefined() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();

        assertNull("Unknown flow should be null", ds.getFlow(0, "A", "B"));
    }

    // ------------------------------------------------------------------------
    // Index and argument validation
    // ------------------------------------------------------------------------

    @Test(expected = IndexOutOfBoundsException.class)
    public void getSources_invalidStage_throws() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.getSources(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getDestinations_invalidStage_throws() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.getDestinations(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFlow_nullSource_throws() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.getFlow(0, null, "B");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInFlows_nullNodeKey_throws() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.getInFlows(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOutFlows_nullNodeKey_throws() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.getOutFlows(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setFlow_negativeStage_throws() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.setFlow(-1, "A", "B", 1.0);
    }

    // ------------------------------------------------------------------------
    // In/out flows
    // ------------------------------------------------------------------------

    @Test
    public void inAndOutFlows_matchAddedFlow() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.setFlow(0, "A", "B", 5.0);

        NodeKey<String> srcKey = new NodeKey<>(0, "A");
        NodeKey<String> dstKey = new NodeKey<>(1, "B"); // destination of stage 0 is in stage index 1
        FlowKey<String> expected = new FlowKey<>(0, "A", "B");

        List<FlowKey<String>> outOfA = ds.getOutFlows(srcKey);
        List<FlowKey<String>> inToB = ds.getInFlows(dstKey);

        assertTrue("Out-flows of A at stage 0 should include A->B", outOfA.contains(expected));
        assertTrue("In-flows of B at stage 1 should include A->B", inToB.contains(expected));
    }

    @Test
    public void inAndOutFlows_emptyForNonMatchingNode() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.setFlow(0, "A", "B", 5.0);

        // Different stage and node that do not participate in any flow
        NodeKey<String> unrelated = new NodeKey<>(0, "X");

        assertTrue("Out-flows for unrelated node should be empty", ds.getOutFlows(unrelated).isEmpty());
        assertTrue("In-flows for unrelated node should be empty", ds.getInFlows(unrelated).isEmpty());
    }

    // ------------------------------------------------------------------------
    // All nodes / all flows
    // ------------------------------------------------------------------------

    @Test
    public void getAllNodes_returnsEveryNodeAtItsStage() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.setFlow(0, "A", "B", 1.0);
        ds.setFlow(1, "B", "C", 2.0);

        Set<NodeKey<String>> allNodes = ds.getAllNodes();

        assertTrue("All nodes should include (0, A)", allNodes.contains(new NodeKey<>(0, "A")));
        assertTrue("All nodes should include (1, B)", allNodes.contains(new NodeKey<>(1, "B")));
        assertTrue("All nodes should include (2, C)", allNodes.contains(new NodeKey<>(2, "C")));
    }

    @Test
    public void getAllFlows_returnsEveryFlowKey() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.setFlow(0, "A", "B", 1.0);
        ds.setFlow(1, "B", "C", 2.0);

        Set<FlowKey<String>> flows = ds.getAllFlows();

        assertTrue("Flow keys should include A->B @0", flows.contains(new FlowKey<>(0, "A", "B")));
        assertTrue("Flow keys should include B->C @1", flows.contains(new FlowKey<>(1, "B", "C")));
    }

    // ------------------------------------------------------------------------
    // Node and Flow properties
    // ------------------------------------------------------------------------

    @Test
    public void nodeProperties_canBeSetAndRead() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        NodeKey<String> a0 = new NodeKey<>(0, "A");

        assertNull("Absent node property returns null", ds.getNodeProperty(a0, "color"));

        ds.setNodeProperty(a0, "color", "red");

        assertEquals("Node property should round-trip", "red", ds.getNodeProperty(a0, "color"));
    }

    @Test
    public void flowProperties_canBeSetAndRead() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();
        ds.setFlow(0, "A", "B", 1.0);
        FlowKey<String> k = new FlowKey<>(0, "A", "B");

        assertNull("Absent flow property returns null", ds.getFlowProperty(k, "label"));

        ds.setFlowProperty(k, "label", "A to B");

        assertEquals("Flow property should round-trip", "A to B", ds.getFlowProperty(k, "label"));
    }

    // ------------------------------------------------------------------------
    // Equality, cloning, and hashCode
    // ------------------------------------------------------------------------

    @Test
    public void equals_emptyDatasetsAreEqual() {
        DefaultFlowDataset<String> a = new DefaultFlowDataset<>();
        DefaultFlowDataset<String> b = new DefaultFlowDataset<>();

        assertTrue("Two empty datasets should be equal", a.equals(b));
        assertEquals("Equal datasets should have the same hashCode", a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_isReflexiveAndNotEqualToOtherTypes() {
        DefaultFlowDataset<String> ds = new DefaultFlowDataset<>();

        assertTrue("Reflexivity", ds.equals(ds));
        assertFalse("Should not equal unrelated type", ds.equals("not a dataset"));
    }

    @Test
    public void clone_producesEqualIndependentCopy() throws Exception {
        DefaultFlowDataset<String> original = new DefaultFlowDataset<>();
        original.setFlow(0, "A", "B", 4.0);

        DefaultFlowDataset<String> copy = (DefaultFlowDataset<String>) original.clone();

        assertTrue("Clone should be equal to original", original.equals(copy));
        assertEquals("Clone should keep stage count", original.getStageCount(), copy.getStageCount());

        // Change original and ensure inequality
        original.setFlow(0, "A", "B", 5.0);

        assertFalse("After modification, original and clone should not be equal", original.equals(copy));
    }

    @Test
    public void equals_detectsDifferentFlowsOrNodes() {
        DefaultFlowDataset<String> a = new DefaultFlowDataset<>();
        DefaultFlowDataset<String> b = new DefaultFlowDataset<>();

        a.setFlow(0, "A", "B", 1.0);
        assertFalse("One flow vs empty should not be equal", a.equals(b));

        b.setFlow(0, "A", "C", 1.0);
        assertFalse("Different destination should not be equal", a.equals(b));
    }
}