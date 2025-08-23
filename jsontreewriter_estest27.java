package com.google.gson.internal.bind;

import static org.junit.Assert.assertTrue;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Contains an improved, more understandable version of the original test for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterImprovedTest {

    /**
     * Verifies that the 'serializeNulls' property, which is true by default,
     * is not altered after writing a simple JSON object.
     */
    @Test
    public void writingAnObjectDoesNotChangeDefaultSerializeNullsProperty() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        // By default, JsonWriter is configured to serialize nulls. This test ensures
        // that this setting is not unexpectedly changed by basic write operations.

        // Act
        // Write a simple, empty JSON object.
        writer.beginObject();
        JsonWriter finishedWriter = writer.endObject();

        // Assert
        assertTrue(
            "The 'serializeNulls' property should remain true after writing an object.",
            finishedWriter.getSerializeNulls()
        );
    }
}