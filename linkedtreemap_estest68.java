package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LinkedTreeMap} class, focusing on its initial state.
 */
public class LinkedTreeMapInitialStateTest {

    /**
     * Verifies that a newly instantiated LinkedTreeMap has a size of zero.
     */
    @Test
    public void newMapShouldBeEmpty() {
        // Arrange: Create a new LinkedTreeMap instance.
        // Using the default constructor with standard types (e.g., String, Integer)
        // is simpler and more readable than using a mock comparator.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act & Assert: Check that the size of the newly created map is 0.
        assertEquals(0, map.size());
    }
}