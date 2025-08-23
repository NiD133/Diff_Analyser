package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import org.junit.Test;

public class JsonTreeReaderTestTest2 {

    @Test
    public void skipValue_onRootJsonObject_consumesObjectAndStopsAtEndDocument() throws IOException {
        // Arrange
        String json = "{"
            + "  \"a\": ['c', \"text\"],"
            + "  \"b\": true,"
            + "  \"i\": 1,"
            + "  \"n\": null,"
            + "  \"o\": {"
            + "    \"n\": 2"
            + "  },"
            + "  \"s\": \"text\""
            + "}";
        JsonElement rootElement = JsonParser.parseString(json);
        JsonTreeReader reader = new JsonTreeReader(rootElement);

        // Act
        reader.skipValue();

        // Assert
        // After skipping the entire root object, the reader should be at the end of the document.
        assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
        // The path should still point to the root, as the reader has consumed the single top-level element.
        assertThat(reader.getPath()).isEqualTo("$");
    }
}