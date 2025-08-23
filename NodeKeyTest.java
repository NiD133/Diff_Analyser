package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable, focused tests for NodeKey.
 * Emphasizes small, intention‑revealing test methods and names.
 */
public class NodeKeyTest {

    // Helper to reduce duplication
    private static NodeKey<String> key(int stage, String node) {
        return new NodeKey<>(stage, node);
    }

    @Test
    @DisplayName("Constructor: rejects null node")
    public void constructorRejectsNullNode() {
        assertThrows(IllegalArgumentException.class, () -> new NodeKey<>(0, null));
    }

    @Test
    @DisplayName("Accessors: return the values supplied to the constructor")
    public void accessorsReturnConstructorValues() {
        NodeKey<String> k = key(3, "A");
        assertAll(
                () -> assertEquals(3, k.getStage(), "stage"),
                () -> assertEquals("A", k.getNode(), "node")
        );
    }

    @Test
    @DisplayName("equals: reflexive and symmetric for identical data")
    public void equals_reflexiveAndSymmetric() {
        NodeKey<String> k1 = key(0, "A");
        NodeKey<String> k2 = key(0, "A");

        assertEquals(k1, k1, "reflexive");
        assertEquals(k1, k2, "symmetric (k1 == k2)");
        assertEquals(k2, k1, "symmetric (k2 == k1)");
    }

    @Test
    @DisplayName("equals: distinguishes different stage")
    public void equals_distinguishesStage() {
        NodeKey<String> a = key(0, "A");
        NodeKey<String> b = key(1, "A");
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("equals: distinguishes different node")
    public void equals_distinguishesNode() {
        NodeKey<String> a = key(1, "A");
        NodeKey<String> b = key(1, "B");
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("equals: handles null and different types")
    public void equals_handlesNullAndDifferentType() {
        NodeKey<String> a = key(0, "A");
        assertNotEquals(a, null);
        assertNotEquals(a, "A");
    }

    @Test
    @DisplayName("hashCode: equal objects have equal hash codes")
    public void hashCode_consistentWithEquals() {
        NodeKey<String> a = key(2, "X");
        NodeKey<String> b = key(2, "X");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("clone: creates a distinct but equal instance")
    @SuppressWarnings("unchecked")
    public void clone_createsEqualButDistinctInstance() throws CloneNotSupportedException {
        NodeKey<String> original = key(2, "A");
        NodeKey<String> copy = (NodeKey<String>) original.clone();

        assertNotSame(original, copy);
        assertSame(original.getClass(), copy.getClass());
        assertEquals(original, copy);
    }

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    public void serialization_roundTripPreservesEquality() {
        NodeKey<String> original = key(1, "S1");
        NodeKey<String> restored = TestUtils.serialised(original);
        assertEquals(original, restored);
    }
}