package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for {@link JsonParser}.
 * This class focuses on improving the understandability of an auto-generated test case.
 */
public class JsonParser_ESTestTest26 {

    /**
     * Verifies that `parseString` correctly handles an unquoted string.
     *
     * <p>The {@link JsonParser} uses a lenient parser by default, which allows
     * unquoted strings. This test ensures that such a string is parsed as a
     * {@link JsonPrimitive} containing a string value.
     */
    @Test
    public void testParseString_givenUnquotedString_returnsStringPrimitive() {
        // Arrange
        String unquotedJson = "A";

        // Act
        JsonElement parsedElement = JsonParser.parseString(unquotedJson);

        // Assert
        // Verify that the result is a JsonPrimitive
        assertTrue("The parsed element should be a JsonPrimitive.", parsedElement.isJsonPrimitive());

        // Cast and perform more specific checks on the primitive
        JsonPrimitive primitive = parsedElement.getAsJsonPrimitive();
        assertTrue("The primitive should be a string.", primitive.isString());
        assertFalse("The primitive should not be a number.", primitive.isNumber());
        assertEquals("The string value should be 'A'.", "A", primitive.getAsString());
    }
}