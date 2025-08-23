package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;
import java.io.StringReader;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link JsonParser} focusing on its error handling with malformed JSON input.
 */
public class JsonParserTest {

    /**
     * Verifies that parsing a string which is not valid JSON (starts with a '}')
     * correctly throws a JsonSyntaxException.
     */
    @Test
    public void parseReader_whenJsonStartsWithClosingBrace_throwsJsonSyntaxException() {
        // Arrange: Create a reader for a malformed JSON string.
        // A valid JSON text must be an object or an array.
        String malformedJson = "}pm";
        StringReader reader = new StringReader(malformedJson);
        JsonReader jsonReader = new JsonReader(reader);

        // Act & Assert: Verify that parsing this reader throws the expected exception.
        JsonSyntaxException thrown = assertThrows(
            JsonSyntaxException.class,
            () -> JsonParser.parseReader(jsonReader) // Using the recommended static method
        );

        // Further verify the exception message for more detailed feedback.
        String expectedMessageFragment = "Expected value at line 1 column 1";
        assertTrue(
            "Exception message should indicate a syntax error at the beginning of the input.",
            thrown.getMessage().contains(expectedMessageFragment)
        );
    }
}