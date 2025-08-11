package org.jfree.data.flow;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link NodeKey}.
 *
 * These tests focus on clear, behavior-driven checks with minimal duplication
 * so that future maintainers can easily understand the intended contract.
 */
public class NodeKeyTest {

    // ----------------------------------------------------------------------
    // Constructor and accessors
    // ----------------------------------------------------------------------

    @Test
    public void constructor_nullNode_throwsIllegalArgumentException() {
        try {
            new NodeKey<>(1, null);
            fail("Expected IllegalArgumentException for null node");
        } catch (IllegalArgumentException expected) {
            // message is set by Args.nullNotPermitted; don't assert exact text to avoid brittleness
        }
    }

    @Test
    public void getStage_returnsProvidedValue() {
        NodeKey<Integer> key = new NodeKey<>(0, Integer.valueOf(42));
        assertEquals(0, key.getStage());
    }

    @Test
    public void getStage_allowsNegativeValues() {
        NodeKey<Integer> key = new NodeKey<>(-1934, Integer.valueOf(1));
        assertEquals(-1934, key.getStage());
    }

    @Test
    public void getNode_returnsProvidedNodeReference() {
        Integer node = Integer.valueOf(504);
        NodeKey<Integer> key = new NodeKey<>(504, node);
        assertSame(node, key.getNode());
    }

    // ----------------------------------------------------------------------
    // equals and hashCode
    // ----------------------------------------------------------------------

    @Test
    public void equals_sameInstance_true() {
        NodeKey<Integer> key = new NodeKey<>(5721, Integer.valueOf(5721));
        assertTrue(key.equals(key));
    }

    @Test
    public void equals_sameStageAndNode_true_andHashCodesMatch() {
        Integer node = Integer.valueOf(3);
        NodeKey<Integer> a = new NodeKey<>(2281, node);
        NodeKey<Integer> b = new NodeKey<>(2281, Integer.valueOf(3)); // equal node value

        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_differentStage_false() {
        Integer node = Integer.valueOf(3);
        NodeKey<Integer> a = new NodeKey<>(2281, node);
        NodeKey<Integer> b = new NodeKey<>(2725, node);

        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void equals_differentNode_false() {
        NodeKey<Integer> a = new NodeKey<>(2281, Integer.valueOf(3));
        NodeKey<Integer> b = new NodeKey<>(2281, Integer.valueOf(2281));

        assertFalse(a.equals(b));
    }

    @Test
    public void equals_nullOrDifferentType_false() {
        NodeKey<Integer> key = new NodeKey<>(504, Integer.valueOf(504));

        assertFalse(key.equals(null));
        assertFalse(key.equals(new Object()));
    }

    // ----------------------------------------------------------------------
    // clone
    // ----------------------------------------------------------------------

    @Test
    public void clone_returnsEqualButDistinctInstance() throws CloneNotSupportedException {
        NodeKey<Integer> original = new NodeKey<>(2281, Integer.valueOf(3));
        NodeKey<?> copy = (NodeKey<?>) original.clone();

        assertNotSame(original, copy);
        assertTrue(original.equals(copy));
        assertEquals(2281, copy.getStage());
        assertEquals(Integer.valueOf(3), copy.getNode());
    }

    // ----------------------------------------------------------------------
    // toString
    // ----------------------------------------------------------------------

    @Test
    public void toString_hasExpectedFormat() {
        NodeKey<Integer> key = new NodeKey<>(2281, Integer.valueOf(3));
        assertEquals("[NodeKey: 2281, 3]", key.toString());
    }

    // ----------------------------------------------------------------------
    // Constants
    // ----------------------------------------------------------------------

    @Test
    public void selectedPropertyKey_constantValue() {
        assertEquals("selected", NodeKey.SELECTED_PROPERTY_KEY);
    }
}