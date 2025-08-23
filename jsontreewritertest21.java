package com.google.gson.internal.bind;

import static org.junit.Assert.assertThrows;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that calling {@link JsonTreeWriter#jsonValue(String)} is not supported.
     *
     * <p>{@code JsonTreeWriter} builds a {@code JsonElement} model in memory and does not support
     * writing raw, pre-encoded JSON strings. This test confirms that an
     * {@link UnsupportedOperationException} is thrown, regardless of the writer's state (e.g.,
     * even when inside an array).
     */
    @Test
    public void jsonValue_throwsUnsupportedOperationException() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray(); // Set the writer to a state where a value is expected

        // Act & Assert: Calling jsonValue should fail because it's an unsupported operation.
        assertThrows(
            UnsupportedOperationException.class,
            () -> writer.jsonValue("any raw json value")
        );
    }
}