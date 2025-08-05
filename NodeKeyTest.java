package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Tests the {@code equals} method to ensure it correctly identifies
     * equality and inequality between different {@code NodeKey} instances.
     */
    @Test
    public void testEquals() {
        // Test equality for identical NodeKey instances
        NodeKey<String> key1 = new NodeKey<>(0, "A");
        NodeKey<String> key2 = new NodeKey<>(0, "A");
        assertEquals(key1, key2, "NodeKeys with same stage and node should be equal");
        assertEquals(key2, key1, "Equality should be symmetric");

        // Test inequality for different stages
        key1 = new NodeKey<>(1, "A");
        assertNotEquals(key1, key2, "NodeKeys with different stages should not be equal");
        
        // Test equality for same stage and node after modification
        key2 = new NodeKey<>(1, "A");
        assertEquals(key1, key2, "NodeKeys with same stage and node should be equal");

        // Test inequality for different nodes
        key1 = new NodeKey<>(1, "B");
        assertNotEquals(key1, key2, "NodeKeys with different nodes should not be equal");
        
        // Test equality for same stage and node after modification
        key2 = new NodeKey<>(1, "B");
        assertEquals(key1, key2, "NodeKeys with same stage and node should be equal");
    }

    /**
     * Tests the cloning functionality to ensure a deep copy of a {@code NodeKey}
     * instance is created.
     *
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        NodeKey<String> originalKey = new NodeKey<>(2, "A");
        NodeKey<String> clonedKey = (NodeKey<String>) originalKey.clone();
        
        assertNotSame(originalKey, clonedKey, "Cloned NodeKey should be a different instance");
        assertSame(originalKey.getClass(), clonedKey.getClass(), "Cloned NodeKey should be of the same class");
        assertEquals(originalKey, clonedKey, "Cloned NodeKey should be equal to the original");
    }

    /**
     * Tests the serialization and deserialization process to ensure the
     * {@code NodeKey} instance remains equal after being serialized and
     * deserialized.
     */
    @Test
    public void testSerialization() {
        NodeKey<String> originalKey = new NodeKey<>(1, "S1");
        NodeKey<String> deserializedKey = TestUtils.serialised(originalKey);
        
        assertEquals(originalKey, deserializedKey, "Deserialized NodeKey should be equal to the original");
    }
}