package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JRadioButtonMenuItem;

/**
 * Test suite for DefaultFlowDataset class.
 * Tests core functionality including flow management, node operations,
 * property handling, and dataset integrity.
 */
public class DefaultFlowDataset_ImprovedTest {

    // Test data constants for better readability
    private static final Integer SOURCE_NODE = 100;
    private static final Integer DESTINATION_NODE = 200;
    private static final Integer ANOTHER_NODE = 300;
    private static final double FLOW_VALUE = 50.0;
    private static final int STAGE_0 = 0;
    private static final int STAGE_1 = 1;

    // ========== Basic Flow Operations ==========

    @Test
    public void testSetAndGetFlow_ValidFlow_ShouldStoreCorrectly() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        dataset.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        
        Number retrievedFlow = dataset.getFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE);
        assertEquals(FLOW_VALUE, retrievedFlow.doubleValue(), 0.001);
        assertEquals(1, dataset.getStageCount());
    }

    @Test
    public void testGetFlow_NonExistentFlow_ShouldReturnNull() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        Number flow = dataset.getFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE);
        
        assertNull("Non-existent flow should return null", flow);
    }

    @Test
    public void testSetFlow_MultipleStages_ShouldIncreaseStageCount() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        dataset.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        dataset.setFlow(STAGE_1, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE * 2);
        
        assertEquals("Should have 2 stages", 2, dataset.getStageCount());
    }

    // ========== Node Operations ==========

    @Test
    public void testGetSources_WithFlowData_ShouldReturnSourceNodes() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        
        List<Integer> sources = dataset.getSources(STAGE_1); // Sources for next stage
        
        assertTrue("Should contain the source node", sources.contains(SOURCE_NODE));
    }

    @Test
    public void testGetDestinations_WithFlowData_ShouldReturnDestinationNodes() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        
        List<Integer> destinations = dataset.getDestinations(STAGE_0);
        
        assertTrue("Should contain the destination node", destinations.contains(DESTINATION_NODE));
    }

    @Test
    public void testGetSources_EmptyDataset_ShouldReturnEmptyList() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        List<Integer> sources = dataset.getSources(STAGE_0);
        
        assertTrue("Empty dataset should return empty source list", sources.isEmpty());
    }

    @Test
    public void testGetDestinations_EmptyDataset_ShouldReturnEmptyList() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        List<Integer> destinations = dataset.getDestinations(STAGE_0);
        
        assertTrue("Empty dataset should return empty destination list", destinations.isEmpty());
    }

    // ========== Flow Key Operations ==========

    @Test
    public void testGetInFlows_ExistingNode_ShouldReturnIncomingFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        NodeKey<Integer> destinationKey = new NodeKey<>(STAGE_1, DESTINATION_NODE);
        
        List<FlowKey<Integer>> inFlows = dataset.getInFlows(destinationKey);
        
        assertFalse("Should have incoming flows", inFlows.isEmpty());
    }

    @Test
    public void testGetOutFlows_ExistingNode_ShouldReturnOutgoingFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        NodeKey<Integer> sourceKey = new NodeKey<>(STAGE_0, SOURCE_NODE);
        
        List<FlowKey> outFlows = dataset.getOutFlows(sourceKey);
        
        assertFalse("Should have outgoing flows", outFlows.isEmpty());
    }

    @Test
    public void testGetInFlows_NonExistentNode_ShouldReturnEmptyList() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        NodeKey<Integer> nodeKey = new NodeKey<>(STAGE_0, ANOTHER_NODE);
        
        List<FlowKey<Integer>> inFlows = dataset.getInFlows(nodeKey);
        
        assertTrue("Non-existent node should have no incoming flows", inFlows.isEmpty());
    }

    @Test
    public void testGetOutFlows_NonExistentNode_ShouldReturnEmptyList() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        NodeKey<Integer> nodeKey = new NodeKey<>(STAGE_0, ANOTHER_NODE);
        
        List<FlowKey> outFlows = dataset.getOutFlows(nodeKey);
        
        assertTrue("Non-existent node should have no outgoing flows", outFlows.isEmpty());
    }

    // ========== Property Management ==========

    @Test
    public void testNodeProperty_SetAndGet_ShouldStoreAndRetrieveCorrectly() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        NodeKey<Integer> nodeKey = new NodeKey<>(STAGE_0, SOURCE_NODE);
        JRadioButtonMenuItem testComponent = new JRadioButtonMenuItem((Icon) null);
        
        dataset.setNodeProperty(nodeKey, "selected", testComponent);
        JRadioButtonMenuItem retrieved = (JRadioButtonMenuItem) dataset.getNodeProperty(nodeKey, "selected");
        
        assertNotNull("Property should be stored", retrieved);
        assertFalse("Component should have expected state", retrieved.isFocusTraversalPolicySet());
    }

    @Test
    public void testFlowProperty_SetAndGet_ShouldStoreAndRetrieveCorrectly() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        FlowKey<Integer> flowKey = new FlowKey<>(STAGE_0, SOURCE_NODE, DESTINATION_NODE);
        
        dataset.setFlowProperty(flowKey, "selected", flowKey);
        FlowKey retrieved = (FlowKey) dataset.getFlowProperty(flowKey, "selected");
        
        assertNotNull("Flow property should be stored", retrieved);
        assertEquals("Should retrieve correct stage", STAGE_0, retrieved.getStage());
    }

    @Test
    public void testGetNodeProperty_NonExistentProperty_ShouldReturnNull() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        NodeKey<Integer> nodeKey = new NodeKey<>(STAGE_0, SOURCE_NODE);
        
        Object property = dataset.getNodeProperty(nodeKey, "nonexistent");
        
        assertNull("Non-existent property should return null", property);
    }

    @Test
    public void testGetFlowProperty_NonExistentProperty_ShouldReturnNull() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        FlowKey<Integer> flowKey = new FlowKey<>(STAGE_0, SOURCE_NODE, DESTINATION_NODE);
        
        Object property = dataset.getFlowProperty(flowKey, "nonexistent");
        
        assertNull("Non-existent flow property should return null", property);
    }

    // ========== Dataset Operations ==========

    @Test
    public void testGetAllFlows_WithData_ShouldReturnAllFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        
        Set<FlowKey<Integer>> allFlows = dataset.getAllFlows();
        
        assertEquals("Should contain one flow", 1, allFlows.size());
    }

    @Test
    public void testGetAllFlows_EmptyDataset_ShouldReturnEmptySet() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        Set<FlowKey<Integer>> allFlows = dataset.getAllFlows();
        
        assertTrue("Empty dataset should return empty flow set", allFlows.isEmpty());
    }

    @Test
    public void testGetAllNodes_WithData_ShouldReturnAllNodes() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        
        Set<NodeKey<Integer>> allNodes = dataset.getAllNodes();
        
        assertFalse("Should contain nodes", allNodes.isEmpty());
    }

    @Test
    public void testGetAllNodes_EmptyDataset_ShouldReturnEmptySet() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        Set<NodeKey<Integer>> allNodes = dataset.getAllNodes();
        
        assertTrue("Empty dataset should return empty node set", allNodes.isEmpty());
    }

    @Test
    public void testGetStageCount_NewDataset_ShouldReturnOne() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        int stageCount = dataset.getStageCount();
        
        assertEquals("New dataset should have 1 stage", 1, stageCount);
    }

    // ========== Clone and Equality Tests ==========

    @Test
    public void testClone_WithData_ShouldCreateEqualCopy() throws CloneNotSupportedException {
        DefaultFlowDataset<Integer> original = new DefaultFlowDataset<>();
        original.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        
        DefaultFlowDataset cloned = (DefaultFlowDataset) original.clone();
        
        assertTrue("Cloned dataset should equal original", original.equals(cloned));
        assertEquals("Cloned dataset should have same stage count", 
                    original.getStageCount(), cloned.getStageCount());
    }

    @Test
    public void testEquals_SameInstance_ShouldReturnTrue() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        boolean isEqual = dataset.equals(dataset);
        
        assertTrue("Dataset should equal itself", isEqual);
    }

    @Test
    public void testEquals_DifferentFlowValues_ShouldReturnFalse() throws CloneNotSupportedException {
        DefaultFlowDataset<Integer> dataset1 = new DefaultFlowDataset<>();
        dataset1.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        
        DefaultFlowDataset<Integer> dataset2 = (DefaultFlowDataset<Integer>) dataset1.clone();
        dataset2.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE * 2);
        
        assertFalse("Datasets with different flow values should not be equal", 
                   dataset1.equals(dataset2));
    }

    @Test
    public void testEquals_DifferentNodes_ShouldReturnFalse() throws CloneNotSupportedException {
        DefaultFlowDataset<Integer> dataset1 = new DefaultFlowDataset<>();
        dataset1.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        
        DefaultFlowDataset<Integer> dataset2 = (DefaultFlowDataset<Integer>) dataset1.clone();
        dataset2.setFlow(STAGE_0, SOURCE_NODE, ANOTHER_NODE, FLOW_VALUE);
        
        assertFalse("Datasets with different nodes should not be equal", 
                   dataset1.equals(dataset2));
    }

    @Test
    public void testEquals_DifferentTypes_ShouldReturnFalse() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        boolean isEqual = dataset.equals("not a dataset");
        
        assertFalse("Dataset should not equal different type", isEqual);
    }

    @Test
    public void testHashCode_WithData_ShouldNotThrow() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(STAGE_0, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
        
        // Should not throw exception
        int hashCode = dataset.hashCode();
        
        // Hash code should be consistent
        assertEquals("Hash code should be consistent", hashCode, dataset.hashCode());
    }

    // ========== Error Handling Tests ==========

    @Test(expected = IllegalArgumentException.class)
    public void testSetFlow_NegativeStage_ShouldThrowException() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        dataset.setFlow(-1, SOURCE_NODE, DESTINATION_NODE, FLOW_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFlow_NullSource_ShouldThrowException() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        dataset.getFlow(STAGE_0, null, DESTINATION_NODE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInFlows_NullNodeKey_ShouldThrowException() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        dataset.getInFlows(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOutFlows_NullNodeKey_ShouldThrowException() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        dataset.getOutFlows(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetSources_InvalidStage_ShouldThrowException() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        dataset.getSources(999); // Invalid stage
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetDestinations_InvalidStage_ShouldThrowException() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        dataset.getDestinations(999); // Invalid stage
    }
}