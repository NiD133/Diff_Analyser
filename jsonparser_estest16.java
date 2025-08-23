package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class JsonParser_ESTestTest16 extends JsonParser_ESTest_scaffolding {

    /**
     * Tests that calling {@link JsonParser#parseReader(JsonReader)} on a reader
     * that has already been fully consumed results in an {@link IllegalStateException}.
     */
    @Test
    public void parseReader_whenCalledOnConsumedReader_throwsIllegalStateException() {
        // Arrange: Create a JsonReader with a single JSON element.
        String json = "\"single-element\"";
        JsonReader jsonReader = new JsonReader(new StringReader(json));

        // Act 1: Consume the first (and only) element from the reader.
        // This moves the reader's cursor to the end of the stream.
        JsonElement firstElement = JsonParser.parseReader(jsonReader);
        assertNotNull("The first parse should successfully return a JsonElement", firstElement);

        // Act 2 & Assert: Attempt to parse from the reader again.
        try {
            JsonParser.parseReader(jsonReader);
            fail("Expected an IllegalStateException because the reader is already at the end of the document.");
        } catch (IllegalStateException expected) {
            // Verify that the exception is thrown for the correct reason.
            assertEquals("Unexpected token: END_DOCUMENT", expected.getMessage());
        }
    }
}