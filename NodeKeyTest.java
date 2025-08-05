package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    @Test
    public void equals_SameInstance_ReturnsTrue() {
        NodeKey<String> key = new NodeKey<>(0, "A");
        assertEquals(key, key);
    }

    @Test
    public void equals_SameStageAndNode_ReturnsTrue() {
        NodeKey<String> k1 = new NodeKey<>(0, "A");
        NodeKey<String> k2 = new NodeKey<>(0, "A");
        assertEquals(k1, k2);
        assertEquals(k2, k1);
    }

    @Test
    public void equals_DifferentStage_ReturnsFalse() {
        NodeKey<String> k1 = new NodeKey<>(0, "A");
        NodeKey<String> k2 = new NodeKey<>(1, "A");
        assertNotEquals(k1, k2);
    }

    @Test
    public void equals_DifferentNode_ReturnsFalse() {
        NodeKey<String> k1 = new NodeKey<>(0, "A");
        NodeKey<String> k2 = new NodeKey<>(0, "B");
        assertNotEquals(k1, k2);
    }

    @Test
    public void equals_NullObject_ReturnsFalse() {
        NodeKey<String> key = new NodeKey<>(0, "A");
        assertNotEquals(null, key);
    }

    @Test
    public void equals_DifferentClass_ReturnsFalse() {
        NodeKey<String> key = new NodeKey<>(0, "A");
        Object other = "Not a NodeKey";
        assertNotEquals(key, other);
    }

    @Test
    public void clone_CreatesNewInstanceWithEqualFields() throws CloneNotSupportedException {
        NodeKey<String> original = new NodeKey<>(2, "A");
        NodeKey<String> clone = (NodeKey<String>) original.clone();
        
        assertNotSame(original, clone);
        assertEquals(original.getStage(), clone.getStage());
        assertEquals(original.getNode(), clone.getNode());
    }

    @Test
    public void serialization_DeserializedInstanceEqualsOriginal() {
        NodeKey<String> original = new NodeKey<>(1, "S1");
        NodeKey<String> deserialized = TestUtils.serialised(original);
        
        assertEquals(original.getStage(), deserialized.getStage());
        assertEquals(original.getNode(), deserialized.getNode());
        assertEquals(original, deserialized);
    }
}