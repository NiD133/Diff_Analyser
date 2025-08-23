package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the {@link LinkedTreeMap} class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class LinkedTreeMap_ESTestTest66 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Verifies that calling `putAll` with the map itself as the source
     * does not alter the map's size. This is because `putAll` should
     * simply overwrite existing keys rather than adding duplicates.
     */
    @Test
    public void putAll_whenCalledWithSelf_shouldNotChangeSize() {
        // Arrange: Create a map and add a single entry.
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        map.put(-1, 100);

        // Sanity check to ensure the initial state is as expected.
        assertEquals("Pre-condition: Map should contain one element", 1, map.size());

        // Act: Call putAll() with the map itself as the argument.
        map.putAll(map);

        // Assert: The size of the map should remain unchanged.
        assertEquals("Post-condition: Map size should still be one", 1, map.size());
    }
}