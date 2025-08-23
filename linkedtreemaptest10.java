package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Tests for {@link LinkedTreeMap} focusing on its handling of null keys.
 */
// Renamed from LinkedTreeMapTestTest10 for clarity and to follow conventions.
public class LinkedTreeMapTest {

    @Test
    public void containsKey_withNullKey_returnsFalse() {
        // Arrange
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();

        // Assert on an empty map
        // According to its contract, LinkedTreeMap does not permit null keys.
        assertThat(map.containsKey(null)).isFalse();

        // Act: Add an element to make the map non-empty.
        map.put("a", "value");

        // Assert on a non-empty map
        // The behavior must be consistent, regardless of the map's contents.
        assertThat(map.containsKey(null)).isFalse();
    }
}