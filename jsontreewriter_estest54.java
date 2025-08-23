package com.google.gson.internal.bind;

import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.Test;

import java.io.IOException;

/**
 * Test suite for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that attempting to call {@code endObject()} when the current
     * context is an array results in an {@link IllegalStateException}.
     * This ensures the writer maintains a valid JSON structure.
     */
    @Test(expected = IllegalStateException.class)
    public void endObject_whenInArray_throwsIllegalStateException() throws IOException {
        // Arrange: Create a writer and start an array.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();

        // Act & Assert: Attempting to end an object should fail.
        writer.endObject();
    }
}