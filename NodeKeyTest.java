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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    // Test data constants for better readability
    private static final int STAGE_0 = 0;
    private static final int STAGE_1 = 1;
    private static final int STAGE_2 = 2;
    private static final String NODE_A = "A";
    private static final String NODE_B = "B";
    private static final String NODE_S1 = "S1";

    /**
     * Tests that the equals method correctly compares NodeKey instances based on both stage and node values.
     * Verifies that equality is symmetric and that changes to either field affect equality.
     */
    @Test
    public void testEquals() {
        // Test: Two identical NodeKeys should be equal
        NodeKey<String> firstKey = new NodeKey<>(STAGE_0, NODE_A);
        NodeKey<String> secondKey = new NodeKey<>(STAGE_0, NODE_A);
        
        assertEquals(firstKey, secondKey, "NodeKeys with same stage and node should be equal");
        assertEquals(secondKey, firstKey, "Equality should be symmetric");

        // Test: NodeKeys with different stages should not be equal
        NodeKey<String> differentStageKey = new NodeKey<>(STAGE_1, NODE_A);
        assertNotEquals(firstKey, differentStageKey, 
            "NodeKeys with different stages should not be equal");
        
        // Update second key to match and verify equality is restored
        secondKey = new NodeKey<>(STAGE_1, NODE_A);
        assertEquals(differentStageKey, secondKey, 
            "NodeKeys with same stage and node should be equal after update");

        // Test: NodeKeys with different nodes should not be equal
        NodeKey<String> differentNodeKey = new NodeKey<>(STAGE_1, NODE_B);
        assertNotEquals(differentStageKey, differentNodeKey, 
            "NodeKeys with different nodes should not be equal");
        
        // Update second key to match and verify equality is restored
        secondKey = new NodeKey<>(STAGE_1, NODE_B);
        assertEquals(differentNodeKey, secondKey, 
            "NodeKeys with same stage and node should be equal after update");
    }

    /**
     * Tests that cloning creates a separate instance with identical content.
     * Verifies that the clone is a different object but has the same class and equals the original.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Given: A NodeKey instance
        NodeKey<String> originalKey = new NodeKey<>(STAGE_2, NODE_A);
        
        // When: Cloning the instance
        NodeKey<String> clonedKey = (NodeKey<String>) originalKey.clone();
        
        // Then: Clone should be a different object but equal in content
        assertNotSame(originalKey, clonedKey, 
            "Cloned NodeKey should be a different object instance");
        assertSame(originalKey.getClass(), clonedKey.getClass(), 
            "Cloned NodeKey should have the same class");
        assertEquals(originalKey, clonedKey, 
            "Cloned NodeKey should be equal to the original");
    }

    /**
     * Tests that NodeKey instances can be serialized and deserialized while maintaining equality.
     * This ensures the class properly implements Serializable.
     */
    @Test
    public void testSerialization() {
        // Given: A NodeKey instance
        NodeKey<String> originalKey = new NodeKey<>(STAGE_1, NODE_S1);
        
        // When: Serializing and deserializing the instance
        NodeKey<String> deserializedKey = TestUtils.serialised(originalKey);
        
        // Then: Deserialized instance should equal the original
        assertEquals(originalKey, deserializedKey, 
            "Deserialized NodeKey should equal the original");
    }
}