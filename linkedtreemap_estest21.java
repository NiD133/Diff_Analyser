package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

public class LinkedTreeMap_ESTestTest21 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that get() throws a NullPointerException if the map is in a corrupt state
     * where the root node has a null key.
     *
     * <p>This is a white-box test that manually manipulates the map's internal state.
     * The public API does not allow inserting null keys, so this state cannot be
     * achieved through normal use. The test ensures that the underlying comparator
     * logic correctly fails when encountering an unexpected null key during a search.
     */
    @Test
    public void get_whenRootNodeHasNullKey_throwsNullPointerException() {
        // Arrange: Create a map and manually set its root to a node with a null key
        // to simulate a corrupt internal state.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        
        // The Node constructor with a single boolean argument creates a "header" node,
        // which has a null key. We use this to create our corrupted root.
        LinkedTreeMap.Node<String, Integer> corruptedRootNode = new LinkedTreeMap.Node<>(true);
        map.root = corruptedRootNode;

        String searchKey = "anyKey";

        // Act & Assert: Attempting to get an element should trigger an NPE when the
        // search key is compared against the root's null key.
        try {
            map.get(searchKey);
            fail("Expected a NullPointerException because the root node's key is null.");
        } catch (NullPointerException e) {
            // This is the expected outcome. The default comparator cannot handle the null key
            // in the corrupted root node. The original exception has no message,
            // so we don't assert on its content.
        }
    }
}