package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import java.util.Map;
import org.junit.Test;

public class LinkedTreeMapTest {

    /**
     * The {@link Map#containsKey(Object)} contract allows for two behaviors when the key is of an
     * inappropriate type for the map: either throw a {@code ClassCastException} or simply return
     * {@code false}.
     *
     * <p>This test verifies that {@code LinkedTreeMap} safely returns {@code false} instead of
     * throwing an exception when the key is not- and cannot be cast to- {@code Comparable}.
     */
    @Test
    public void containsKey_withNonComparableKey_returnsFalse() {
        // Arrange
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "value"); // Ensure the map is not empty.
        Object nonComparableKey = new Object();

        // Act
        boolean result = map.containsKey(nonComparableKey);

        // Assert
        assertThat(result).isFalse();
    }
}