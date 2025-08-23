package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link LinkedTreeMap}.
 * This class was renamed from LinkedTreeMapTestTest3 for clarity.
 */
public class LinkedTreeMapTest {

    @Test
    public void put_withNullKey_throwsNullPointerException() {
        // Arrange
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        String value = "some value";

        // Act & Assert
        // The `put` method is expected to throw a NullPointerException when the key is null.
        NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> map.put(null, value)
        );

        // Verify that the exception has the expected message.
        assertThat(exception).hasMessageThat().isEqualTo("key == null");
    }
}