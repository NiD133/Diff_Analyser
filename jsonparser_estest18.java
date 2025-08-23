package com.google.gson;

import static org.junit.Assert.fail;

import com.google.gson.JsonSyntaxException;
import org.junit.Test;

/**
 * Test suite for {@link JsonParser}.
 */
public class JsonParserTest {

    @Test
    public void parsingUnquotedStringShouldThrowJsonSyntaxException() {
        // Arrange: An unquoted string is not a valid JSON document.
        // According to the JSON standard, a top-level value that is a string
        // must be enclosed in double quotes.
        String malformedJson = "This is not valid JSON.";

        // Act & Assert
        try {
            // Use the modern static `parseString` method, as the instance-based `parse` is deprecated.
            JsonParser.parseString(malformedJson);
            fail("Expected JsonParser.parseString to throw a JsonSyntaxException for malformed input, but it did not.");
        } catch (JsonSyntaxException expected) {
            // Success: The expected exception was correctly thrown.
            // We are only verifying that parsing this specific type of malformed JSON fails,
            // so no further assertions on the exception's content are needed here.
        }
    }
}