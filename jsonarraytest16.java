package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link JsonArray} to ensure correct handling of null elements.
 */
public class JsonArrayTest {

    @Test
    public void add_whenNullJsonElementIsAdded_itIsConvertedToJsonNull() {
        // Arrange
        JsonArray jsonArray = new JsonArray();

        // Act
        // The explicit cast to JsonElement is necessary to resolve the ambiguous
        // `add(JsonElement)` and `add(String)` overloads when passing `null`.
        jsonArray.add((JsonElement) null);

        // Assert
        assertThat(jsonArray.size()).isEqualTo(1);
        assertThat(jsonArray.get(0)).isEqualTo(JsonNull.INSTANCE);
    }
}