package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.Strictness;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that closing a {@link JsonTreeWriter} before all JSON structures
     * (like arrays or objects) are properly closed results in an {@link IOException}.
     * This ensures the writer enforces the creation of a complete and valid JSON document,
     * even when configured with lenient strictness.
     */
    @Test
    public void close_whenDocumentIsIncomplete_throwsIOException() throws IOException {
        // Arrange: Create a writer and begin an array, but do not close it.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LENIENT);
        writer.beginArray();

        // Act: Attempt to close the writer while the JSON structure is incomplete.
        IOException exception = assertThrows(IOException.class, writer::close);

        // Assert: Verify that an IOException is thrown with a specific message.
        assertThat(exception).hasMessageThat().isEqualTo("Incomplete document");
    }
}