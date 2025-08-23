package com.google.gson.internal.bind;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link JsonTreeWriter} focusing on invalid state transitions.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that attempting to write a value directly into a JSON object
     * without first calling {@code name()} throws an {@link IllegalStateException}.
     * According to the JSON specification, values within an object must be part of a name-value pair.
     */
    @Test(expected = IllegalStateException.class)
    public void writingValueInObjectWithoutName_shouldThrowIllegalStateException() throws IOException {
        // Arrange: Create a writer and start a JSON object.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();

        // Act: Attempt to write a boolean value without a preceding name.
        // This is an invalid operation for an object context.
        writer.value(false);

        // Assert: An IllegalStateException is expected, verified by the @Test annotation.
    }
}