package com.google.gson.internal;

import org.junit.Test;

/**
 * This class contains the test case from the original file.
 * The surrounding test suite structure is kept for context.
 */
public class LinkedTreeMap_ESTestTest19 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that calling put() on a map with a manually corrupted root node
     * (specifically, a root with a null key) throws a NullPointerException.
     *
     * This scenario tests the robustness of the put method when faced with an
     * invalid internal state, which should not occur in normal operation but
     * could happen due to unforeseen bugs or reflection-based manipulation.
     */
    @Test(expected = NullPointerException.class)
    public void put_whenRootNodeHasNullKey_throwsNullPointerException() {
        // Arrange: Create a map and simulate a corrupted state by manually setting
        // its root to a node that has a null key. The internal implementation
        // of LinkedTreeMap expects the root node to always have a non-null key.
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        
        // This creates a special-purpose node, normally used only for the map's header,
        // which has a null key.
        LinkedTreeMap.Node<Integer, String> rootWithNullKey = new LinkedTreeMap.Node<>(false);
        map.root = rootWithNullKey;

        // Act: Attempt to add an element to the map. The put operation will try to
        // compare the new key with the root's null key, causing the NPE.
        map.put(1, "one");

        // Assert: The @Test(expected) annotation verifies that a NullPointerException was thrown.
    }
}