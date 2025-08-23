package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for the {@link JsonArray#toString()} method.
 */
public class JsonArrayToStringTest {

    @Test
    public void toString_onEmptyArray_returnsEmptyBrackets() {
        // Arrange
        JsonArray array = new JsonArray();

        // Act
        String jsonOutput = array.toString();

        // Assert
        assertThat(jsonOutput).isEqualTo("[]");
    }

    @Test
    public void toString_withVariousElementTypes_returnsCorrectlyFormattedJson() {
        // Arrange: Create an array with various element types to test serialization,
        // especially those requiring special handling.
        JsonArray array = new JsonArray();

        // 1. A JSON null value
        array.add(JsonNull.INSTANCE);
        // 2. A non-finite number (NaN is serialized as-is by Gson's toString())
        array.add(Float.NaN);
        // 3. A string with a null control character, which must be Unicode-escaped
        array.add("a\0");

        // 4. A nested array containing a string with a quote character, which must be backslash-escaped
        JsonArray nestedArray = new JsonArray();
        nestedArray.add('"'); // Adds the character '"' as a JsonPrimitive
        array.add(nestedArray);

        // 5. A nested object with a key containing a null control character
        JsonObject nestedObject = new JsonObject();
        nestedObject.addProperty("n\0", 1);
        array.add(nestedObject);

        // Act
        String jsonOutput = array.toString();

        // Assert
        // The expected string should correctly represent all special values and structures:
        // [null,NaN,"a\u0000",["\""],{"n\u0000":1}]
        String expectedJson = "[null,NaN,\"a\\u0000\",[\"\\\"\"],{\"n\\u0000\":1}]";
        assertThat(jsonOutput).isEqualTo(expectedJson);
    }
}