package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JsonParser}.
 */
public class JsonParserTest {

    /**
     * Tests that attempting to parse from a JsonReader that has already been fully consumed
     * results in an IllegalStateException. The parser should not be able to read a second
     * top-level value if only one exists.
     */
    @Test
    public void parseReader_onExhaustedReader_throwsIllegalStateException() {
        // Arrange: Create a reader for a single, complete JSON document.
        String json = "\"a single json value\"";
        JsonReader jsonReader = new JsonReader(new StringReader(json));

        // Act 1: Consume the first (and only) JSON element from the reader.
        // This advances the reader to the end of the stream.
        JsonParser.parseReader(jsonReader);

        // Act 2 & Assert: Attempting to parse again from the same, now-exhausted reader
        // should fail because it finds an END_DOCUMENT token instead of a new value.
        try {
            JsonParser.parseReader(jsonReader);
            fail("Expected an IllegalStateException to be thrown when parsing an exhausted reader.");
        } catch (IllegalStateException expected) {
            assertEquals("Unexpected token: END_DOCUMENT", expected.getMessage());
        }
    }
}