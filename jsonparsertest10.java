package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link JsonParser}.
 */
public class JsonParserTest {

    private static final int DEEP_NESTING_DEPTH = 10_000;
    private static final String NESTED_OBJECT_KEY = "a";

    /**
     * Deeply nested JSON objects should not cause {@link StackOverflowError}.
     */
    @Test
    public void parseReader_withDeeplyNestedObject_doesNotCauseStackOverflow() throws IOException {
        // Arrange
        // Create a deeply nested JSON string: {"a":{"a":...{"a":null}...}}
        String json = "{\"" + NESTED_OBJECT_KEY + "\":".repeat(DEEP_NESTING_DEPTH)
            + "null"
            + "}".repeat(DEEP_NESTING_DEPTH);

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        // Increase the nesting limit to avoid a JsonIOException.
        // The goal is to test for a potential StackOverflowError, not the nesting limit feature.
        jsonReader.setNestingLimit(Integer.MAX_VALUE);

        // Act
        JsonElement rootElement = JsonParser.parseReader(jsonReader);

        // Assert
        // Traverse the parsed structure to verify its depth and integrity.
        int actualNestingDepth = 0;
        JsonObject currentObject = rootElement.getAsJsonObject();

        while (true) {
            assertThat(currentObject.size()).isEqualTo(1);
            actualNestingDepth++;

            JsonElement nextElement = currentObject.get(NESTED_OBJECT_KEY);
            if (nextElement.isJsonNull()) {
                break; // Reached the innermost value
            } else {
                currentObject = nextElement.getAsJsonObject();
            }
        }

        assertThat(actualNestingDepth).isEqualTo(DEEP_NESTING_DEPTH);
    }
}