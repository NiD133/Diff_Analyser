package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jfree.data.flow.NodeKey;

/**
 * Test suite for the NodeKey class.
 * Tests the core functionality including construction, equality, cloning, and accessor methods.
 */
public class NodeKeyTest {

    // Test data constants
    private static final int STAGE_ZERO = 0;
    private static final int STAGE_POSITIVE = 2281;
    private static final int STAGE_NEGATIVE = -1934;
    private static final Integer NODE_VALUE_ZERO = Integer.valueOf(0);
    private static final Integer NODE_VALUE_THREE = Integer.valueOf(3);

    // Constructor Tests
    
    @Test
    public void testConstructor_WithValidParameters_ShouldCreateNodeKey() {
        NodeKey<Integer> nodeKey = new NodeKey<>(STAGE_POSITIVE, NODE_VALUE_THREE);
        
        assertEquals("Stage should be set correctly", STAGE_POSITIVE, nodeKey.getStage());
        assertEquals("Node should be set correctly", NODE_VALUE_THREE, nodeKey.getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithNullNode_ShouldThrowException() {
        new NodeKey<Integer>(617, null);
    }

    // Accessor Method Tests
    
    @Test
    public void testGetStage_WithZeroStage_ShouldReturnZero() {
        NodeKey<Integer> nodeKey = new NodeKey<>(STAGE_ZERO, NODE_VALUE_ZERO);
        
        assertEquals("Should return stage value of 0", STAGE_ZERO, nodeKey.getStage());
    }

    @Test
    public void testGetStage_WithNegativeStage_ShouldReturnNegativeValue() {
        NodeKey<Integer> nodeKey = new NodeKey<>(STAGE_NEGATIVE, NODE_VALUE_ZERO);
        
        assertEquals("Should return negative stage value", STAGE_NEGATIVE, nodeKey.getStage());
    }

    @Test
    public void testGetNode_ShouldReturnNodeValue() {
        NodeKey<Integer> nodeKey = new NodeKey<>(504, Integer.valueOf(504));
        
        assertNotNull("Node should not be null", nodeKey.getNode());
        assertEquals("Should return correct node value", Integer.valueOf(504), nodeKey.getNode());
    }

    // Equality Tests
    
    @Test
    public void testEquals_WithSameInstance_ShouldReturnTrue() {
        NodeKey<Integer> nodeKey = new NodeKey<>(STAGE_POSITIVE, NODE_VALUE_THREE);
        
        assertTrue("NodeKey should equal itself", nodeKey.equals(nodeKey));
    }

    @Test
    public void testEquals_WithIdenticalNodeKeys_ShouldReturnTrue() {
        NodeKey<Integer> nodeKey1 = new NodeKey<>(STAGE_POSITIVE, NODE_VALUE_THREE);
        NodeKey<Integer> nodeKey2 = new NodeKey<>(STAGE_POSITIVE, NODE_VALUE_THREE);
        
        assertTrue("NodeKeys with same stage and node should be equal", nodeKey1.equals(nodeKey2));
    }

    @Test
    public void testEquals_WithDifferentStages_ShouldReturnFalse() {
        NodeKey<Integer> nodeKey1 = new NodeKey<>(STAGE_POSITIVE, NODE_VALUE_THREE);
        NodeKey<Integer> nodeKey2 = new NodeKey<>(2725, NODE_VALUE_THREE);
        
        assertFalse("NodeKeys with different stages should not be equal", nodeKey1.equals(nodeKey2));
    }

    @Test
    public void testEquals_WithDifferentNodes_ShouldReturnFalse() {
        NodeKey<Integer> nodeKey1 = new NodeKey<>(STAGE_POSITIVE, NODE_VALUE_THREE);
        NodeKey<Integer> nodeKey2 = new NodeKey<>(STAGE_POSITIVE, Integer.valueOf(2281));
        
        assertFalse("NodeKeys with different nodes should not be equal", nodeKey1.equals(nodeKey2));
    }

    @Test
    public void testEquals_WithDifferentStagesAndNodes_ShouldReturnFalse() {
        NodeKey<Integer> nodeKey1 = new NodeKey<>(STAGE_NEGATIVE, NODE_VALUE_ZERO);
        NodeKey<Integer> nodeKey2 = new NodeKey<>(-3799, NODE_VALUE_ZERO);
        
        assertFalse("NodeKeys with different stages should not be equal", nodeKey1.equals(nodeKey2));
    }

    @Test
    public void testEquals_WithNull_ShouldReturnFalse() {
        NodeKey<Integer> nodeKey = new NodeKey<>(504, Integer.valueOf(504));
        
        assertFalse("NodeKey should not equal null", nodeKey.equals(null));
    }

    @Test
    public void testEquals_WithDifferentObjectType_ShouldReturnFalse() {
        NodeKey<Integer> nodeKey = new NodeKey<>(STAGE_POSITIVE, NODE_VALUE_THREE);
        Object differentObject = new Object();
        
        assertFalse("NodeKey should not equal different object type", nodeKey.equals(differentObject));
    }

    // Clone Tests
    
    @Test
    public void testClone_ShouldCreateEqualCopy() throws CloneNotSupportedException {
        NodeKey<Integer> original = new NodeKey<>(STAGE_POSITIVE, NODE_VALUE_THREE);
        NodeKey<Integer> cloned = (NodeKey<Integer>) original.clone();
        
        assertTrue("Cloned NodeKey should equal original", original.equals(cloned));
        assertEquals("Cloned NodeKey should have same stage", original.getStage(), cloned.getStage());
        assertEquals("Cloned NodeKey should have same node", original.getNode(), cloned.getNode());
    }

    @Test
    public void testClone_WithDifferentOriginalValues_ShouldMaintainIndependence() throws CloneNotSupportedException {
        NodeKey<Integer> nodeKey1 = new NodeKey<>(-3799, NODE_VALUE_ZERO);
        NodeKey<Integer> cloned = (NodeKey<Integer>) nodeKey1.clone();
        NodeKey<Integer> nodeKey2 = new NodeKey<>(STAGE_NEGATIVE, NODE_VALUE_ZERO);
        
        assertFalse("Cloned NodeKey should not equal different NodeKey", nodeKey2.equals(cloned));
        assertEquals("Cloned NodeKey should maintain original stage", -3799, cloned.getStage());
    }

    // String Representation Tests
    
    @Test
    public void testToString_ShouldReturnFormattedString() {
        NodeKey<Integer> nodeKey = new NodeKey<>(STAGE_POSITIVE, NODE_VALUE_THREE);
        String result = nodeKey.toString();
        
        assertEquals("Should return formatted string representation", 
                     "[NodeKey: 2281, 3]", result);
    }

    // Hash Code Tests
    
    @Test
    public void testHashCode_ShouldExecuteWithoutException() {
        NodeKey<Integer> nodeKey = new NodeKey<>(9, Integer.valueOf(9));
        
        // Just verify hashCode can be called without exception
        int hashCode = nodeKey.hashCode();
        // Hash code value itself is implementation-dependent, so we just ensure it executes
        assertNotNull("Hash code calculation should complete", Integer.valueOf(hashCode));
    }
}