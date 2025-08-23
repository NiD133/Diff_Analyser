package com.google.gson.internal.bind;

import org.junit.Test;
import java.io.IOException;

/**
 * Test suite for {@link JsonTreeWriter}.
 * This focuses on validating the correct state transitions and error handling.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that attempting to write a value inside a JSON object without first
     * specifying a name throws an {@link IllegalStateException}. According to the JSON
     * specification, an object must consist of name-value pairs.
     */
    @Test(expected = IllegalStateException.class)
    public void writeValue_insideObjectWithoutName_throwsIllegalStateException() throws IOException {
        // Arrange: Create a writer and start a JSON object.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();

        // Act: Attempt to write a value without a preceding name.
        // This is an invalid operation and is expected to throw.
        writer.value(-1.0);
    }
}