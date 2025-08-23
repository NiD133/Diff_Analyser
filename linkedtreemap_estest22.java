package com.google.gson.internal;

import org.junit.Test;

/**
 * Unit tests for the {@link LinkedTreeMap} class, focusing on specific method behaviors.
 */
public class LinkedTreeMapTest {

    /**
     * Verifies that the {@code findByEntry} method throws a {@link NullPointerException}
     * when passed a null entry. This is the expected behavior as the method needs to
     * access the entry's key to perform a lookup.
     */
    @Test(expected = NullPointerException.class)
    public void findByEntry_whenEntryIsNull_shouldThrowNullPointerException() {
        // Arrange: Create an empty LinkedTreeMap.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act & Assert: Calling findByEntry with a null argument should throw.
        // The @Test(expected = ...) annotation handles the assertion.
        map.findByEntry(null);
    }
}