package com.google.gson.internal.bind;

import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that calling jsonValue() is not supported and throws an exception.
     * <p>
     * The {@link JsonTreeWriter} builds a {@link com.google.gson.JsonElement} model in memory
     * and does not support parsing and embedding a raw JSON string directly.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void jsonValue_shouldThrowUnsupportedOperationException() {
        // Arrange
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

        // Act
        jsonTreeWriter.jsonValue("{\"key\":\"value\"}");

        // Assert is handled by the 'expected' attribute of the @Test annotation
    }
}