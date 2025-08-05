package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A collection of tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    @Test
    public void constructor_shouldSetStageAndNode() {
        // Arrange
        int stage = 1;
        String node = "Node A";

        // Act
        NodeKey<String> key = new NodeKey<>(stage, node);

        // Assert
        assertEquals("Stage should match the value from the constructor.", stage, key.getStage());
        assertEquals("Node should match the value from the constructor.", node, key.getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNullNode_shouldThrowIllegalArgumentException() {
        // Act: Attempt to create a NodeKey with a null node, which is not allowed.
        new NodeKey<>(1, (String) null);
    }

    @Test
    public void equals_withSameInstance_shouldReturnTrue() {
        // Arrange
        NodeKey<String> key = new NodeKey<>(1, "A");

        // Act & Assert: An object must be equal to itself (reflexivity).
        assertTrue("A key must be equal to itself.", key.equals(key));
    }

    @Test
    public void equals_withEqualInstances_shouldReturnTrue() {
        // Arrange
        NodeKey<String> key1 = new NodeKey<>(1, "A");
        NodeKey<String> key2 = new NodeKey<>(1, "A");

        // Act & Assert: Two separate instances with the same state should be equal.
        assertTrue("Keys with the same stage and node should be equal.", key1.equals(key2));
        assertTrue("Equality should be symmetric.", key2.equals(key1));
    }

    @Test
    public void equals_withNullObject_shouldReturnFalse() {
        // Arrange
        NodeKey<String> key = new NodeKey<>(1, "A");

        // Act & Assert: An object must not be equal to null.
        assertFalse("A key must not be equal to null.", key.equals(null));
    }

    @Test
    public void equals_withDifferentObjectType_shouldReturnFalse() {
        // Arrange
        NodeKey<String> key = new NodeKey<>(1, "A");
        Object otherObject = new Object();

        // Act & Assert
        assertFalse("A key must not be equal to an object of a different type.", key.equals(otherObject));
    }

    @Test
    public void equals_withDifferentStages_shouldReturnFalse() {
        // Arrange
        NodeKey<String> key1 = new NodeKey<>(1, "A");
        NodeKey<String> key2 = new NodeKey<>(2, "A"); // Same node, different stage

        // Act & Assert
        assertFalse("Keys with different stages should not be equal.", key1.equals(key2));
    }

    @Test
    public void equals_withDifferentNodes_shouldReturnFalse() {
        // Arrange
        NodeKey<String> key1 = new NodeKey<>(1, "A");
        NodeKey<String> key2 = new NodeKey<>(1, "B"); // Same stage, different node

        // Act & Assert
        assertFalse("Keys with different nodes should not be equal.", key1.equals(key2));
    }

    @Test
    public void hashCode_forEqualObjects_shouldBeEqual() {
        // Arrange
        NodeKey<String> key1 = new NodeKey<>(1, "A");
        NodeKey<String> key2 = new NodeKey<>(1, "A");

        // Act & Assert: Per the hashCode contract, equal objects must have equal hash codes.
        assertEquals("Hash codes of equal objects must be equal.", key1.hashCode(), key2.hashCode());
    }

    @Test
    public void clone_shouldReturnEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange
        NodeKey<String> original = new NodeKey<>(1, "A");

        // Act
        NodeKey<String> clone = (NodeKey<String>) original.clone();

        // Assert
        assertNotSame("Cloned object should be a different instance from the original.", original, clone);
        assertEquals("Cloned object should be equal to the original.", original, clone);
    }

    @Test
    public void toString_shouldReturnCorrectStringRepresentation() {
        // Arrange
        NodeKey<Integer> key = new NodeKey<>(2281, 3);

        // Act
        String keyString = key.toString();

        // Assert
        assertEquals("toString() should return a predictable, formatted string.", "[NodeKey: 2281, 3]", keyString);
    }
}