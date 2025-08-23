package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link JsonParser}.
 * This improved version replaces an auto-generated test case.
 */
public class JsonParserTest {

    @Test
    public void parseString_withUnquotedString_parsesAsStringPrimitive() {
        // Arrange
        // In lenient mode (the default for JsonParser), unquoted text is parsed as a string.
        String jsonWithUnquotedString = "N";

        // Act
        // Use the modern static `parseString` method instead of the deprecated instance method.
        JsonElement parsedElement = JsonParser.parseString(jsonWithUnquotedString);

        // Assert
        // The original test only checked that the result was not a number.
        // A more robust test also verifies its actual type and value.
        assertTrue("The parsed element should be a JsonPrimitive.", parsedElement.isJsonPrimitive());

        JsonPrimitive primitive = parsedElement.getAsJsonPrimitive();
        assertTrue("The primitive should be a string.", primitive.isString());
        assertEquals("The string value should be 'N'.", "N", primitive.getAsString());
        assertFalse("The primitive should not be a number.", primitive.isNumber());
    }
}