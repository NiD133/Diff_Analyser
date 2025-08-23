package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

public class JsonParserTest {

    /**
     * Tests that parsing a JSON array containing an unquoted string from a Reader
     * succeeds. This is because the parser operates in lenient mode by default,
     * which allows for such syntax.
     */
    @Test
    public void testParseReaderWithUnquotedStringInArray() {
        // Arrange
        String jsonWithUnquotedString = "[GoyG.G]";
        Reader reader = new StringReader(jsonWithUnquotedString);

        // Act
        // Use the static `parseReader` method, as the JsonParser constructor and
        // instance `parse` methods are deprecated.
        JsonElement parsedElement = JsonParser.parseReader(reader);

        // Assert
        assertTrue("The parsed element should be a JsonArray.", parsedElement.isJsonArray());
        JsonArray resultArray = parsedElement.getAsJsonArray();

        assertFalse("The resulting array should not be empty.", resultArray.isEmpty());
        assertEquals("The array should contain exactly one element.", 1, resultArray.size());

        JsonElement element = resultArray.get(0);
        assertEquals("The element's value should match the unquoted string.", "GoyG.G", element.getAsString());
    }
}