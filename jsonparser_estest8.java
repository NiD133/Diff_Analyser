package com.google.gson;

import static org.junit.Assert.assertEquals;

import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Contains tests for {@link JsonParser}, focusing on its lenient parsing behavior.
 */
public class JsonParserTest {

    /**
     * Tests that parsing an unquoted string, which is invalid in strict JSON,
     * is handled leniently by being parsed as a JsonPrimitive containing that string.
     * This is the default behavior of the parser.
     */
    @Test
    public void parseReader_withUnquotedString_parsesAsJsonPrimitiveString() {
        // Arrange
        String unquotedString = "M(";
        Reader reader = new StringReader(unquotedString);
        JsonElement expectedElement = new JsonPrimitive(unquotedString);

        // Act
        // The parseReader method uses a lenient JsonReader by default,
        // which allows it to successfully parse unquoted strings.
        JsonElement actualElement = JsonParser.parseReader(reader);

        // Assert
        assertEquals(expectedElement, actualElement);
    }
}