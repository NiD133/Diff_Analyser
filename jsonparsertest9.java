package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class JsonParserTestTest9 {

    /**
     * Verifies that parsing a deeply nested JSON array does not cause a {@link StackOverflowError}.
     * The default recursion limit of {@link JsonReader} is disabled for this test to specifically
     * test the behavior of {@link JsonParser} itself.
     */
    @Test
    public void parseReader_deeplyNestedArray_avoidsStackOverflow() throws IOException {
        // Arrange
        int nestingDepth = 10000;
        // Creates a JSON string with deeply nested arrays, e.g., "[[[]]]" for nestingDepth=3
        String json = "[".repeat(nestingDepth) + "]".repeat(nestingDepth);

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        // Disable JsonReader's default nesting limit to isolate and test JsonParser's behavior
        jsonReader.setNestingLimit(Integer.MAX_VALUE);

        // Act
        JsonElement parsedElement = JsonParser.parseReader(jsonReader);

        // Assert
        // Verify that the parsed structure has the expected nesting depth by traversing it.
        assertThat(parsedElement.isJsonArray()).isTrue();
        JsonArray currentArray = parsedElement.getAsJsonArray();

        // Traverse down the nested arrays, from the outermost to the second-to-innermost
        for (int i = 1; i < nestingDepth; i++) {
            // Each outer array must contain exactly one element: the next inner array
            assertThat(currentArray.size()).isEqualTo(1);
            currentArray = currentArray.get(0).getAsJsonArray();
        }

        // The innermost array should be empty
        assertThat(currentArray.isEmpty()).isTrue();
    }
}