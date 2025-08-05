package org.jfree.data.flow;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * A collection of tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    private DefaultFlowDataset<String> dataset;

    private static final String NODE_A = "Node A";
    private static final String NODE_B = "Node B";
    private static final String NODE_C = "Node C";

    @Before
    public void setUp() {
        dataset = new DefaultFlowDataset<>();
    }

    @Test
    public void newDataset_hasOneStageInitially() {
        // A new dataset is expected to have one stage by default.
        assertEquals(1, dataset.getStageCount());
    }

    @Test
    public void newDataset_hasNoNodesOrFlows() {
        // A new dataset should be empty.
        assertTrue("Sources for stage 0 should be empty", dataset.getSources(0).isEmpty());
        assertTrue("Destinations for stage 0 should be empty", dataset.getDestinations(0).isEmpty());
        assertTrue("All nodes set should be empty", dataset.getAllNodes().isEmpty());
        assertTrue("All flows set should be empty", dataset.getAllFlows().isEmpty());
    }

    @Test
    public void setFlow_addsNodesAndFlowValue() {
        // Arrange
        double flowValue = 100.0;

        // Act
        dataset.setFlow(0, NODE_A, NODE_B, flowValue);

        // Assert
        // Check flow value
        assertEquals(flowValue, dataset.getFlow(0, NODE_A, NODE_B).doubleValue(), 0.001);
        
        // Check nodes
        assertTrue(dataset.getSources(0).contains(NODE_A));
        assertTrue(dataset.getDestinations(0).contains(NODE_B));
        
        // The destination of stage 0 becomes a source for stage 1
        assertTrue(dataset.getSources(1).contains(NODE_B));
    }

    @Test
    public void setFlow_increasesStageCountWhenAddingToNewStage() {
        // Arrange
        dataset.setFlow(0, NODE_A, NODE_B, 100.0);
        assertEquals(1, dataset.getStageCount());

        // Act: Add a flow to the next stage
        dataset.setFlow(1, NODE_B, NODE_C, 50.0);

        // Assert
        assertEquals(2, dataset.getStageCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setFlow_throwsExceptionForNegativeStage() {
        // Act
        dataset.setFlow(-1, NODE_A, NODE_B, 100.0);
    }

    @Test
    public void getFlow_returnsNullForNonExistentFlow() {
        // Act & Assert
        assertNull(dataset.getFlow(0, NODE_A, NODE_B));
    }

    @Test
    public void getInFlows_returnsCorrectFlowsForNode() {
        // Arrange
        dataset.setFlow(0, NODE_A, NODE_B, 100.0);
        dataset.setFlow(0, NODE_C, NODE_B, 50.0); // Another flow into NODE_B
        NodeKey<String> nodeB_at_stage1 = new NodeKey<>(1, NODE_B);
        
        // Act
        List<FlowKey<String>> inFlows = dataset.getInFlows(nodeB_at_stage1);

        // Assert
        assertEquals(2, inFlows.size());
        assertTrue(inFlows.contains(new FlowKey<>(0, NODE_A, NODE_B)));
        assertTrue(inFlows.contains(new FlowKey<>(0, NODE_C, NODE_B)));
    }

    @Test
    public void getOutFlows_returnsCorrectFlowsForNode() {
        // Arrange
        dataset.setFlow(0, NODE_A, NODE_B, 100.0);
        dataset.setFlow(0, NODE_A, NODE_C, 50.0); // Another flow from NODE_A
        NodeKey<String> nodeA_at_stage0 = new NodeKey<>(0, NODE_A);

        // Act
        List<FlowKey<String>> outFlows = dataset.getOutFlows(nodeA_at_stage0);

        // Assert
        assertEquals(2, outFlows.size());
        assertTrue(outFlows.contains(new FlowKey<>(0, NODE_A, NODE_B)));
        assertTrue(outFlows.contains(new FlowKey<>(0, NODE_A, NODE_C)));
    }
    
    @Test
    public void getInFlows_returnsEmptyListForNodeWithNoInflows() {
        // Arrange
        dataset.setFlow(0, NODE_A, NODE_B, 100.0);
        NodeKey<String> nodeA_at_stage1 = new NodeKey<>(1, NODE_A); // Node A has no inflows

        // Act
        List<FlowKey<String>> inFlows = dataset.getInFlows(nodeA_at_stage1);

        // Assert
        assertTrue(inFlows.isEmpty());
    }

    @Test
    public void getAllNodes_returnsAllNodesFromAllStages() {
        // Arrange
        dataset.setFlow(0, NODE_A, NODE_B, 100.0);
        dataset.setFlow(1, NODE_B, NODE_C, 50.0);

        // Act
        Set<NodeKey<String>> allNodes = dataset.getAllNodes();

        // Assert
        assertEquals(3, allNodes.size());
        assertTrue(allNodes.contains(new NodeKey<>(0, NODE_A)));
        assertTrue(allNodes.contains(new NodeKey<>(1, NODE_B)));
        assertTrue(allNodes.contains(new NodeKey<>(2, NODE_C)));
    }

    @Test
    public void getAllFlows_returnsAllAddedFlows() {
        // Arrange
        dataset.setFlow(0, NODE_A, NODE_B, 100.0);
        dataset.setFlow(1, NODE_B, NODE_C, 50.0);
        
        // Act
        Set<FlowKey<String>> allFlows = dataset.getAllFlows();

        // Assert
        assertEquals(2, allFlows.size());
        assertTrue(allFlows.contains(new FlowKey<>(0, NODE_A, NODE_B)));
        assertTrue(allFlows.contains(new FlowKey<>(1, NODE_B, NODE_C)));
    }

    @Test
    public void nodeProperties_canBeSetAndRetrieved() {
        // Arrange
        NodeKey<String> nodeKey = new NodeKey<>(0, NODE_A);
        String propertyKey = "color";
        String propertyValue = "blue";

        // Act
        dataset.setNodeProperty(nodeKey, propertyKey, propertyValue);
        Object retrievedValue = dataset.getNodeProperty(nodeKey, propertyKey);

        // Assert
        assertEquals(propertyValue, retrievedValue);
    }

    @Test
    public void flowProperties_canBeSetAndRetrieved() {
        // Arrange
        FlowKey<String> flowKey = new FlowKey<>(0, NODE_A, NODE_B);
        String propertyKey = "label";
        String propertyValue = "Main Flow";

        // Act
        dataset.setFlowProperty(flowKey, propertyKey, propertyValue);
        Object retrievedValue = dataset.getFlowProperty(flowKey, propertyKey);

        // Assert
        assertEquals(propertyValue, retrievedValue);
    }

    @Test
    public void equals_isReflexive() {
        // A dataset must be equal to itself.
        assertTrue(dataset.equals(dataset));
    }

    @Test
    public void equals_returnsTrueForIdenticalDatasets() {
        // Arrange
        dataset.setFlow(0, NODE_A, NODE_B, 100.0);

        DefaultFlowDataset<String> otherDataset = new DefaultFlowDataset<>();
        otherDataset.setFlow(0, NODE_A, NODE_B, 100.0);

        // Act & Assert
        assertTrue(dataset.equals(otherDataset));
        assertEquals(dataset.hashCode(), otherDataset.hashCode());
    }

    @Test
    public void equals_returnsFalseForDifferentFlowValues() {
        // Arrange
        dataset.setFlow(0, NODE_A, NODE_B, 100.0);

        DefaultFlowDataset<String> otherDataset = new DefaultFlowDataset<>();
        otherDataset.setFlow(0, NODE_A, NODE_B, 99.0); // Different value

        // Act & Assert
        assertFalse(dataset.equals(otherDataset));
    }

    @Test
    public void equals_returnsFalseForDifferentFlowStructures() {
        // Arrange
        dataset.setFlow(0, NODE_A, NODE_B, 100.0);

        DefaultFlowDataset<String> otherDataset = new DefaultFlowDataset<>();
        otherDataset.setFlow(0, NODE_A, NODE_C, 100.0); // Different destination

        // Act & Assert
        assertFalse(dataset.equals(otherDataset));
    }

    @Test
    public void equals_returnsFalseForDifferentStageCounts() {
        // Arrange
        dataset.setFlow(1, NODE_A, NODE_B, 100.0); // Creates 2 stages

        DefaultFlowDataset<String> otherDataset = new DefaultFlowDataset<>();
        otherDataset.setFlow(0, NODE_A, NODE_B, 100.0); // Creates 1 stage

        // Act & Assert
        assertFalse(dataset.equals(otherDataset));
    }

    @Test
    public void equals_returnsFalseForDifferentObjectType() {
        // An object should not be equal to an object of a different type.
        assertFalse(dataset.equals(new Object()));
    }

    @Test
    public void clone_producesEqualAndIndependentObject() throws CloneNotSupportedException {
        // Arrange
        dataset.setFlow(0, NODE_A, NODE_B, 100.0);
        dataset.setNodeProperty(new NodeKey<>(0, NODE_A), "prop", "value");

        // Act
        DefaultFlowDataset<String> clonedDataset = (DefaultFlowDataset<String>) dataset.clone();

        // Assert
        assertNotSame("Clone should be a different object instance", dataset, clonedDataset);
        assertEquals("Clone should be equal to the original", dataset, clonedDataset);

        // Verify independence by modifying the original
        dataset.setFlow(0, NODE_A, NODE_B, 200.0);
        assertFalse("Modifying original should not affect the clone", dataset.equals(clonedDataset));
    }
}