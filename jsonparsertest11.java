package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import java.io.StringReader;
import org.junit.Test;

/**
 * Unit tests for {@link JsonParser}.
 */
public class JsonParserTest {

    @Test
    public void parseReader_withLenientJson_succeeds() {
        // Arrange
        // JsonParser is lenient by default, so it should handle non-standard JSON
        // like unquoted keys and single-quoted string values.
        String lenientJson = "{a:10,b:'c'}";
        StringReader reader = new StringReader(lenientJson);

        // Act
        JsonElement parsedElement = JsonParser.parseReader(reader);

        // Assert
        assertThat(parsedElement.isJsonObject()).isTrue();
        JsonObject jsonObject = parsedElement.getAsJsonObject();
        assertThat(jsonObject.get("a").getAsInt()).isEqualTo(10);
        assertThat(jsonObject.get("b").getAsString()).isEqualTo("c");
    }
}