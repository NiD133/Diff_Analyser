package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

// The original test class structure is kept for context.
public class JsonParser_ESTestTest3 extends JsonParser_ESTest_scaffolding {

    /**
     * Tests that `parseReader` correctly handles an unquoted string by parsing it
     * as a JsonPrimitive string, which is the expected behavior in lenient mode.
     */
    @Test
    public void parseReader_withUnquotedString_parsesAsJsonPrimitiveString() {
        // Arrange
        String unquotedString = "MUB_qz @d";
        Reader jsonReader = new StringReader(unquotedString);

        // Act
        JsonElement parsedElement = JsonParser.parseReader(jsonReader);

        // Assert
        // Verify that the parser correctly identified the element as a JsonPrimitive
        assertTrue("The parsed element should be a JsonPrimitive.", parsedElement.isJsonPrimitive());

        // Verify that the primitive is a string with the correct value
        JsonPrimitive jsonPrimitive = parsedElement.getAsJsonPrimitive();
        assertTrue("The JsonPrimitive should be a string.", jsonPrimitive.isString());
        assertEquals("The string value of the JsonPrimitive is incorrect.", unquotedString, jsonPrimitive.getAsString());
    }
}