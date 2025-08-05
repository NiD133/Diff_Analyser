/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ----------------
 * NodeKeyTest.java
 * ----------------
 * (C) Copyright 2021-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link NodeKey} class.
 */
@DisplayName("A NodeKey")
class NodeKeyTest {

    @Nested
    @DisplayName("constructor")
    class Constructor {
        @Test
        @DisplayName("should correctly initialize stage and node")
        void constructor_shouldStoreArguments() {
            // Arrange
            int expectedStage = 1;
            String expectedNode = "A";

            // Act
            NodeKey<String> key = new NodeKey<>(expectedStage, expectedNode);

            // Assert
            assertAll("Properties should match constructor arguments",
                () -> assertEquals(expectedStage, key.getStage()),
                () -> assertEquals(expectedNode, key.getNode())
            );
        }

        @Test
        @DisplayName("should throw an exception for a null node")
        void constructor_withNullNode_shouldThrowException() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new NodeKey<>(1, null);
            }, "Constructor should not permit a null node.");
        }
    }

    @Nested
    @DisplayName("equals() and hashCode() contract")
    class EqualsAndHashCode {

        @Test
        @DisplayName("equals() should return true for two instances with the same state")
        void equals_withSameState_shouldReturnTrue() {
            // Arrange
            NodeKey<String> key1 = new NodeKey<>(0, "A");
            NodeKey<String> key2 = new NodeKey<>(0, "A");

            // Act & Assert
            assertEquals(key1, key2, "Keys with the same stage and node should be equal.");
        }

        @Test
        @DisplayName("hashCode() should be consistent for equal objects")
        void hashCode_forEqualObjects_shouldBeSame() {
            // Arrange
            NodeKey<String> key1 = new NodeKey<>(0, "A");
            NodeKey<String> key2 = new NodeKey<>(0, "A");

            // Act & Assert
            assertEquals(key1.hashCode(), key2.hashCode(), "Hash codes must be identical for equal objects.");
        }

        @Test
        @DisplayName("equals() should return false for different stages")
        void equals_withDifferentStage_shouldReturnFalse() {
            // Arrange
            NodeKey<String> key1 = new NodeKey<>(0, "A");
            NodeKey<String> key2 = new NodeKey<>(1, "A");

            // Act & Assert
            assertNotEquals(key1, key2, "Keys with different stages should not be equal.");
        }

        @Test
        @DisplayName("equals() should return false for different nodes")
        void equals_withDifferentNode_shouldReturnFalse() {
            // Arrange
            NodeKey<String> key1 = new NodeKey<>(0, "A");
            NodeKey<String> key2 = new NodeKey<>(0, "B");

            // Act & Assert
            assertNotEquals(key1, key2, "Keys with different nodes should not be equal.");
        }

        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            // Arrange
            NodeKey<String> key = new NodeKey<>(0, "A");

            // Act & Assert
            assertNotEquals(null, key, "A key should not be equal to null.");
        }

        @Test
        @DisplayName("equals() should return false for different object types")
        void equals_withDifferentObjectType_shouldReturnFalse() {
            // Arrange
            NodeKey<String> key = new NodeKey<>(0, "A");
            Object otherObject = "Not a NodeKey";

            // Act & Assert
            assertNotEquals(key, otherObject, "A key should not be equal to an object of a different type.");
        }
    }

    @Test
    @DisplayName("clone() should create an independent copy")
    void clone_shouldReturnIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        NodeKey<String> original = new NodeKey<>(2, "A");

        // Act
        NodeKey<String> clone = (NodeKey<String>) original.clone();

        // Assert
        assertNotSame(original, clone, "The clone should be a different instance from the original.");
        assertEquals(original, clone, "The clone should be equal to the original in state.");
    }

    @Test
    @DisplayName("should be serializable")
    void serialization_shouldPreserveState() {
        // Arrange
        NodeKey<String> original = new NodeKey<>(1, "S1");

        // Act
        NodeKey<String> deserialized = TestUtils.serialised(original);

        // Assert
        assertEquals(original, deserialized, "The deserialized key should be equal to the original.");
    }
}