package com.google.gson.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Contains tests for the equals() and hashCode() contracts of {@link LinkedTreeMap}.
 */
public class LinkedTreeMapTest {

    /**
     * Verifies that two maps with the same entries are equal, regardless of insertion order,
     * and consequently have the same hash code. This behavior is required by the
     * {@link java.util.Map} interface contract.
     */
    @Test
    public void mapsWithSameEntriesButDifferentInsertionOrderShouldBeEqualAndHaveSameHashCode() {
        // Arrange: Create two maps with identical key-value pairs but inserted in a different sequence.
        LinkedTreeMap<String, Integer> map1 = new LinkedTreeMap<>();
        map1.put("A", 1);
        map1.put("B", 2);
        map1.put("C", 3);
        map1.put("D", 4);

        LinkedTreeMap<String, Integer> map2 = new LinkedTreeMap<>();
        map2.put("C", 3);
        map2.put("B", 2);
        map2.put("D", 4);
        map2.put("A", 1);

        // Assert: The maps should be considered equal and must have the same hash code.
        // We use standard JUnit assertions for clarity and better failure messages.

        // 1. Test for equality.
        // The Map.equals() contract is based on the entry sets being equal, not insertion order.
        assertEquals(map1, map2);

        // 2. Test for hash code consistency.
        // If two objects are equal, their hash codes must also be equal.
        assertEquals(map1.hashCode(), map2.hashCode());
    }
}