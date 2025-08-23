package com.google.gson.internal.bind;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that attempting to write a value directly into a JSON object,
     * without first specifying a name for it, results in an IllegalStateException.
     * According to the JSON specification, all values within an object must be
     * part of a name/value pair.
     */
    @Test
    public void writingValueInsideObjectWithoutNameThrowsException() {
        // Arrange: Create a writer and start a JSON object.
        JsonTreeWriter jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();

        // Act & Assert: Attempting to write a value without a preceding name should fail.
        assertThrows(IllegalStateException.class, () -> {
            // The specific value type (e.g., a null Number) is not important;
            // any `value()` call would be invalid at this point.
            jsonWriter.value((Number) null);
        });
    }
}