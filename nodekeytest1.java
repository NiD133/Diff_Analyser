package org.jfree.data.flow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link NodeKey} class, focusing on its value-based equality
 * and hashCode implementation.
 */
@DisplayName("NodeKey")
class NodeKeyTest {

    @Nested
    @DisplayName("equals() and hashCode() contract")
    class EqualsAndHashCodeContract {

        @Test
        @DisplayName("A key is equal to itself")
        void keyIsEqualToItself() {
            // Arrange
            NodeKey<String> key = new NodeKey<>(0, "A");

            // Act & Assert
            assertEquals(key, key, "A key should always be equal to itself (reflexivity).");
        }

        @Test
        @DisplayName("Keys with the same stage and node are equal")
        void keysWithSameStateAreEqual() {
            // Arrange
            NodeKey<String> key1 = new NodeKey<>(1, "B");
            NodeKey<String> key2 = new NodeKey<>(1, "B");

            // Act & Assert
            assertTrue(key1.equals(key2), "Keys with the same stage and node should be equal.");
            assertTrue(key2.equals(key1), "Equality should be symmetric.");
        }

        @Test
        @DisplayName("Equal keys have the same hash code")
        void equalKeysHaveSameHashCode() {
            // Arrange
            NodeKey<String> key1 = new NodeKey<>(1, "B");
            NodeKey<String> key2 = new NodeKey<>(1, "B");

            // Act & Assert
            assertEquals(key1.hashCode(), key2.hashCode(), "Equal objects must have the same hash code.");
        }

        @Test
        @DisplayName("Keys with different stages are not equal")
        void keysWithDifferentStagesAreNotEqual() {
            // Arrange
            NodeKey<String> key1 = new NodeKey<>(0, "A");
            NodeKey<String> key2 = new NodeKey<>(1, "A"); // Different stage

            // Act & Assert
            assertNotEquals(key1, key2);
        }

        @Test
        @DisplayName("Keys with different nodes are not equal")
        void keysWithDifferentNodesAreNotEqual() {
            // Arrange
            NodeKey<String> key1 = new NodeKey<>(0, "A");
            NodeKey<String> key2 = new NodeKey<>(0, "B"); // Different node

            // Act & Assert
            assertNotEquals(key1, key2);
        }

        @Test
        @DisplayName("A key is not equal to null")
        void keyIsNotEqualToNull() {
            // Arrange
            NodeKey<String> key = new NodeKey<>(0, "A");

            // Act & Assert
            assertNotEquals(null, key, "A key should never be equal to null.");
        }

        @Test
        @DisplayName("A key is not equal to an object of a different type")
        void keyIsNotEqualToObjectOfDifferentType() {
            // Arrange
            NodeKey<String> key = new NodeKey<>(0, "A");
            Object otherObject = "Not a NodeKey";

            // Act & Assert
            assertNotEquals(key, otherObject, "A key should not be equal to an object of a different class.");
        }
    }
}