package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import org.junit.Test;

/**
 * Test for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void skipValue_onEmptyJsonObject_reachesEndDocument() throws IOException {
        // Arrange: Create a reader for an empty JSON object.
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());

        // Act: Skip the entire value, which is the object itself.
        reader.skipValue();

        // Assert: The reader should now be at the end of the document.
        assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
        // The path should reflect the root object that was just consumed.
        assertThat(reader.getPath()).isEqualTo("$");
    }
}