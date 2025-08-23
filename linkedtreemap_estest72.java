package com.google.gson.internal;

import org.junit.Test;
import java.util.Iterator;
import java.util.Set;
import static org.junit.Assert.*;

/**
 * This test class contains tests for the LinkedTreeMap class.
 * This specific test focuses on the behavior of the KeySet iterator.
 */
public class LinkedTreeMapTest {

    /**
     * Verifies that calling iterator() on the keySet of an empty map
     * returns a valid, empty iterator.
     */
    @Test
    public void keySetIterator_onEmptyMap_returnsEmptyIterator() {
        // Arrange: Create an empty LinkedTreeMap and get its key set.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        Set<String> keySet = map.keySet();

        // Act: Get an iterator from the key set.
        Iterator<String> iterator = keySet.iterator();

        // Assert: The iterator should be non-null and have no elements.
        // The key set itself should remain empty.
        assertNotNull("The iterator should not be null.", iterator);
        assertFalse("The iterator for an empty set should not have a next element.", iterator.hasNext());
        assertEquals("The size of the key set should be 0.", 0, keySet.size());
    }
}