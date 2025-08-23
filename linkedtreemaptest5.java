package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link LinkedTreeMap}.
 *
 * <p>This test focuses on the behavior of putting entries with null values.
 */
public class LinkedTreeMapTest {

    /**
     * Verifies that putting a null value is allowed by default and that the map's
     * state is updated correctly. The default constructor of {@link LinkedTreeMap}
     * is expected to permit null values.
     */
    @Test
    public void put_withNullValue_isAllowedByDefault() {
        // Arrange
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        String key = "a";

        // Act
        map.put(key, null);

        // Assert
        // Verify the map's state after the operation
        assertThat(map).hasSize(1);
        assertThat(map.containsKey(key)).isTrue();
        assertThat(map.containsValue(null)).isTrue();
        assertThat(map.get(key)).isNull();
    }
}