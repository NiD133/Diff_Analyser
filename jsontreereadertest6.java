package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * This test verifies that calling {@code skipValue()} when the reader is positioned
     * at the end of an object's content (i.e., where {@code END_OBJECT} is the next token)
     * effectively consumes the entire object.
     *
     * This is a non-standard way to consume an object, as one would normally call
     * {@code endObject()}.
     */
    @Test
    public void skipValueAtEndOfObjectConsumesObject() throws IOException {
        // Arrange: Create a reader for an empty JSON object and advance it past the opening brace.
        JsonObject emptyObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(emptyObject);
        reader.beginObject(); // Reader is now inside the object, expecting END_OBJECT

        // Act: Instead of calling endObject(), call skipValue().
        reader.skipValue();

        // Assert: The reader should have consumed the object and reached the end of the document.
        assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
        assertThat(reader.getPath()).isEqualTo("$");
    }
}