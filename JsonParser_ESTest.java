package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.Strictness;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Unit tests for {@link JsonParser}.
 * Focuses on verifying the parsing of valid, lenient, and malformed JSON
 * from various sources (String, Reader, JsonReader).
 */
public class JsonParserTest {

    //region parseString(String) tests

    @Test
    public void parseString_withValidObject_returnsJsonObject() {
        String json = "{\"key\":\"value\"}";
        JsonElement result = JsonParser.parseString(json);
        assertTrue(result.isJsonObject());
        assertEquals("value", result.getAsJsonObject().get("key").getAsString());
    }

    @Test
    public void parseString_withValidArray_returnsJsonArray() {
        String json = "[1, \"two\", false]";
        JsonElement result = JsonParser.parseString(json);
        assertTrue(result.isJsonArray());
        assertEquals(1, result.getAsJsonArray().get(0).getAsInt());
    }

    @Test
    public void parseString_withEmptyString_returnsJsonNull() {
        // For compatibility with earlier versions, parsing an empty string is treated
        // as a special case and results in a JsonNull.
        JsonElement result = JsonParser.parseString("");
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void parseString_withLenientUnquotedString_returnsJsonPrimitive() {
        // The parser operates in lenient mode, so it can handle unquoted strings.
        JsonElement result = JsonParser.parseString("A");
        assertTrue(result.isJsonPrimitive());
        assertEquals("A", result.getAsString());
    }

    @Test
    public void parseString_withLenientArray_returnsJsonArray() {
        // Lenient mode also allows unquoted strings within arrays.
        JsonElement result = JsonParser.parseString("[GoyG.G]");
        assertTrue(result.isJsonArray());
        assertFalse(result.getAsJsonArray().isEmpty());
    }

    @Test(expected = JsonSyntaxException.class)
    public void parseString_withTrailingData_throwsJsonSyntaxException() {
        // parseString requires the input to be a single, complete JSON element.
        // Trailing data is not permitted.
        JsonParser.parseString("{} \"extra data\"");
    }

    @Test(expected = NullPointerException.class)
    public void parseString_withNullArgument_throwsNullPointerException() {
        JsonParser.parseString(null);
    }

    //endregion

    //region parseReader(Reader) tests

    @Test
    public void parseReader_fromReader_withEmptyInput_returnsJsonNull() {
        Reader emptyReader = new StringReader("");
        JsonElement result = JsonParser.parseReader(emptyReader);
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void parseReader_fromReader_withLenientString_returnsJsonPrimitive() {
        Reader reader = new StringReader("unquoted_string");
        JsonElement result = JsonParser.parseReader(reader);
        assertTrue(result.isJsonPrimitive());
        assertEquals("unquoted_string", result.getAsString());
    }

    @Test(expected = JsonSyntaxException.class)
    public void parseReader_fromReader_withMalformedJson_throwsJsonSyntaxException() {
        Reader reader = new StringReader("=invalid");
        JsonParser.parseReader(reader);
    }

    @Test(expected = NullPointerException.class)
    public void parseReader_fromReader_withNullArgument_throwsNullPointerException() {
        JsonParser.parseReader((Reader) null);
    }

    //endregion

    //region parseReader(JsonReader) tests

    @Test
    public void parseReader_fromJsonReader_parsesFirstElementAndIgnoresTrailingData() {
        // Unlike parseString or parseReader(Reader), parseReader(JsonReader) only
        // consumes the next available JSON element from the stream.
        JsonReader jsonReader = new JsonReader(new StringReader("{} extra data"));
        JsonElement result = JsonParser.parseReader(jsonReader);
        assertTrue(result.isJsonObject());
        // The "extra data" remains unconsumed in the reader.
    }

    @Test
    public void parseReader_fromJsonReader_onEmptyReader_returnsJsonNull() {
        // When a JsonReader is created on an empty source, it is immediately at
        // the end of the document. Parsing from it returns JsonNull.
        JsonReader jsonReader = new JsonReader(new StringReader(""));
        JsonElement result = JsonParser.parseReader(jsonReader);
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void parseReader_fromJsonReader_inStrictMode_throwsExceptionForMalformedJson() {
        JsonReader jsonReader = new JsonReader(new StringReader("unquoted"));
        jsonReader.setStrictness(Strictness.STRICT);

        try {
            JsonParser.parseReader(jsonReader);
            fail("Expected JsonSyntaxException for malformed JSON in strict mode");
        } catch (JsonSyntaxException expected) {
            // The parser should respect the reader's strictness setting.
            assertNotNull(expected.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void parseReader_fromJsonReader_calledAfterStreamIsExhausted_throwsIllegalStateException() {
        JsonReader jsonReader = new JsonReader(new StringReader("\"a single value\""));
        JsonParser.parseReader(jsonReader); // First call succeeds and consumes the value.
        JsonParser.parseReader(jsonReader); // Second call finds END_DOCUMENT and throws.
    }

    @Test(expected = NullPointerException.class)
    public void parseReader_fromJsonReader_withNullArgument_throwsNullPointerException() {
        JsonParser.parseReader((JsonReader) null);
    }

    //endregion

    //region Deprecated instance method tests

    @Test
    public void parse_deprecatedInstanceMethods_delegateToStaticMethods() {
        JsonParser parser = new JsonParser();

        // Test parse(String)
        JsonElement fromString = parser.parse("true");
        assertEquals(new JsonPrimitive(true), fromString);

        // Test parse(Reader)
        JsonElement fromReader = parser.parse(new StringReader("123"));
        assertEquals(new JsonPrimitive(123), fromReader);

        // Test parse(JsonReader)
        JsonReader jsonReader = new JsonReader(new StringReader("[]"));
        JsonElement fromJsonReader = parser.parse(jsonReader);
        assertEquals(new JsonArray(), fromJsonReader);
    }

    //endregion
}