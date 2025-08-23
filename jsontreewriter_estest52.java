package com.google.gson.internal.bind;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that calling name() twice in a row within a JSON object is illegal.
     * The writer expects a value to be written after a name is specified.
     */
    @Test
    public void writingTwoConsecutiveNamesInObjectThrowsException() throws IOException {
        // Arrange: Start writing a JSON object and add the first property name.
        JsonTreeWriter jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();
        jsonWriter.name("property1"); // This is a valid state.

        // Act & Assert: Attempting to write a second name should fail.
        try {
            jsonWriter.name("property2");
            fail("Expected IllegalStateException was not thrown. A name must be followed by a value.");
        } catch (IllegalStateException expected) {
            // Assert that the correct exception with the expected message was thrown.
            assertEquals("Did not expect a name", expected.getMessage());
        }
    }
}