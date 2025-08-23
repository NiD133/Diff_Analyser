package com.google.gson.internal.bind;

import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that calling {@link JsonTreeWriter#endObject()} without a preceding
     * call to {@link JsonTreeWriter#beginObject()} throws an {@link IllegalStateException}.
     * This ensures the writer enforces a valid JSON object structure.
     */
    @Test(expected = IllegalStateException.class)
    public void endObject_whenNotInsideObject_throwsIllegalStateException() {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Attempt to end an object that was never started.
        // Assert: The @Test(expected) annotation asserts that an IllegalStateException is thrown.
        writer.endObject();
    }
}