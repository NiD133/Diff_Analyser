package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Test class for {@link JsonParser}.
 */
public class JsonParserTest {

    @Test
    public void parseReader_withTrailingJunk_parsesFirstJsonElementSuccessfully() {
        // Arrange
        // The parseReader(JsonReader) method is designed to parse the next available
        // JSON element and ignore any subsequent, non-JSON characters.
        String jsonWithTrailingJunk = "{}/GH6t)T:8sOB3 #";
        JsonReader jsonReader = new JsonReader(new StringReader(jsonWithTrailingJunk));

        // Act
        JsonElement parsedElement = JsonParser.parseReader(jsonReader);

        // Assert
        assertTrue("The parsed element should be a JsonObject", parsedElement.isJsonObject());
        // Verify it's the correct empty object, not a malformed one.
        assertEquals(new JsonObject(), parsedElement);
    }
}