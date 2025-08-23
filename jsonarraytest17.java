package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Tests for {@link JsonArray} focusing on element addition behavior.
 */
public class JsonArrayTestTest17 {

    @Test
    public void testArrayCanContainDuplicateElements() {
        // Arrange
        JsonArray jsonArray = new JsonArray();

        // Act: Add various types of values, each one twice, to confirm that
        // duplicates are permitted.
        jsonArray.add('a');
        jsonArray.add('a');

        jsonArray.add(true);
        jsonArray.add(true);

        jsonArray.add(1212);
        jsonArray.add(1212);

        jsonArray.add(34.34);
        jsonArray.add(34.34);

        // Note: JsonArray converts null arguments to JsonNull instances.
        jsonArray.add((Boolean) null);
        jsonArray.add((Boolean) null);

        // Assert
        // Verify that all duplicate elements were added and are present in the final JSON string.
        String expectedJson = "[\"a\",\"a\",true,true,1212,1212,34.34,34.34,null,null]";
        assertThat(jsonArray.toString()).isEqualTo(expectedJson);
    }
}