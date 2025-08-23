package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link JsonArray} focusing on element addition.
 */
public class JsonArrayTest {

    @Test
    public void testAdd_withMixedTypes_createsCorrectJsonString() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        String expectedJson = "[\"a\",\"e\",\"i\",\"o\",null,\"u\",\"and sometimes Y\"]";

        // Act: Add a mix of characters, a null Character, and a String.
        // The test verifies that each type is correctly converted and added.
        jsonArray.add('a');
        jsonArray.add('e');
        jsonArray.add('i');
        jsonArray.add('o');
        jsonArray.add((Character) null); // A null Character should be converted to JsonNull.
        jsonArray.add('u');
        jsonArray.add("and sometimes Y");

        // Assert
        assertThat(jsonArray.toString()).isEqualTo(expectedJson);
    }
}