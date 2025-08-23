package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.IOException;
import org.junit.Test;

public class JsonTreeWriterTestTest2 {

    @Test
    public void writeNestedArrays_producesCorrectJsonTree() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Build the JSON structure for [[], [[]]]
        writer.beginArray();    // Outer array starts
        writer.beginArray();    // First inner array starts
        writer.endArray();      // First inner array ends
        writer.beginArray();    // Second inner array starts
        writer.beginArray();    // Innermost array starts
        writer.endArray();      // Innermost array ends
        writer.endArray();      // Second inner array ends
        writer.endArray();      // Outer array ends

        JsonElement actualJson = writer.get();

        // Assert: Verify the generated JsonElement matches the expected structure
        JsonArray expectedOuterArray = new JsonArray();
        expectedOuterArray.add(new JsonArray()); // Add the first empty inner array: []

        JsonArray secondInnerArray = new JsonArray();
        secondInnerArray.add(new JsonArray()); // Add the innermost empty array: [[]]
        expectedOuterArray.add(secondInnerArray);

        assertThat(actualJson).isEqualTo(expectedOuterArray);
    }
}