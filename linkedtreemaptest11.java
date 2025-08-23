package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link LinkedTreeMap}, focusing on the behavior of the {@code put} method.
 */
public final class LinkedTreeMapTestTest11 {

    @Test
    public void put_withExistingKey_replacesValueAndReturnsOldValue() {
        // Arrange: Create a map and populate it with initial values.
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        
        // The `put` method should return null when adding a new key-value pair.
        assertThat(map.put("d", "donut")).isNull();
        assertThat(map.put("e", "eclair")).isNull();
        assertThat(map.put("f", "froyo")).isNull();
        
        // Sanity check the initial state before the main action.
        assertThat(map).hasSize(3);
        assertThat(map.get("d")).isEqualTo("donut");

        // Act: Replace the value for an existing key.
        String previousValue = map.put("d", "done");

        // Assert: Verify the outcomes of the put operation.
        // It should return the value that was replaced.
        assertThat(previousValue).isEqualTo("donut");
        
        // The size of the map should not change.
        assertThat(map).hasSize(3);
        
        // The map should now contain the new value for the key.
        assertThat(map.get("d")).isEqualTo("done");
    }
}