package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * Test suite for JsonParser functionality including static methods and deprecated instance methods.
 * Tests cover successful parsing, error handling, and edge cases.
 */
public class JsonParserTest {

    // ========== Static Method Tests - parseString() ==========

    @Test
    public void parseString_withEmptyString_returnsJsonNull() {
        JsonElement result = JsonParser.parseString("");
        
        assertTrue("Empty string should parse to JsonNull", result.isJsonNull());
    }

    @Test
    public void parseString_withValidJsonArray_returnsJsonArray() {
        String jsonArrayString = "[\"GoyG.G\"]";
        
        JsonElement result = JsonParser.parseString(jsonArrayString);
        
        assertTrue("Valid JSON array should parse to JsonArray", result.isJsonArray());
    }

    @Test
    public void parseString_withSingleCharacter_returnsJsonPrimitive() {
        String singleChar = "\"A\"";
        
        JsonPrimitive result = (JsonPrimitive) JsonParser.parseString(singleChar);
        
        assertFalse("Single character should not be a number", result.isNumber());
    }

    @Test(expected = NullPointerException.class)
    public void parseString_withNullInput_throwsNullPointerException() {
        JsonParser.parseString(null);
    }

    @Test(expected = RuntimeException.class)
    public void parseString_withMalformedJson_throwsRuntimeException() {
        String malformedJson = "lP ?";
        
        JsonParser.parseString(malformedJson);
    }

    // ========== Static Method Tests - parseReader(Reader) ==========

    @Test
    public void parseReader_withStringReader_returnsJsonPrimitive() {
        StringReader reader = new StringReader("\"MUB_qz@d\"");
        
        JsonPrimitive result = (JsonPrimitive) JsonParser.parseReader(reader);
        
        assertFalse("String primitive should not be boolean", result.isBoolean());
    }

    @Test
    public void parseReader_withEmptyReader_returnsJsonNull() {
        StringReader emptyReader = new StringReader("");
        
        JsonElement result = JsonParser.parseReader(emptyReader);
        
        assertFalse("Empty reader should not return JsonArray", result.isJsonArray());
        assertTrue("Empty reader should return JsonNull", result.isJsonNull());
    }

    @Test(expected = NullPointerException.class)
    public void parseReader_withNullReader_throwsNullPointerException() {
        JsonParser.parseReader((Reader) null);
    }

    @Test(expected = RuntimeException.class)
    public void parseReader_withInvalidJson_throwsRuntimeException() {
        StringReader invalidReader = new StringReader("=PC x;DM1DK}c/;");
        
        JsonParser.parseReader(invalidReader);
    }

    // ========== Static Method Tests - parseReader(JsonReader) ==========

    @Test
    public void parseReader_withJsonReaderContainingObject_returnsJsonObject() {
        StringReader stringReader = new StringReader("{}3+1M-tu;%QX!~Y");
        JsonReader jsonReader = new JsonReader(stringReader);
        
        JsonElement result = JsonParser.parseReader(jsonReader);
        
        assertFalse("Result should not be JsonNull", result.isJsonNull());
        assertTrue("Result should be JsonObject", result.isJsonObject());
    }

    @Test
    public void parseReader_withJsonReaderContainingArray_returnsJsonArray() {
        StringReader stringReader = new StringReader("[\"GoyG.G\"]");
        JsonReader jsonReader = new JsonReader(stringReader);
        
        JsonElement result = JsonParser.parseReader(jsonReader);
        
        assertFalse("Result should not be JsonPrimitive", result.isJsonPrimitive());
        assertTrue("Result should be JsonArray", result.isJsonArray());
    }

    @Test
    public void parseReader_withStrictMode_throwsExceptionForMalformedJson() {
        StringReader stringReader = new StringReader("MUB_qz@d");
        JsonReader jsonReader = new JsonReader(stringReader);
        jsonReader.setStrictness(Strictness.STRICT);
        
        try {
            JsonParser.parseReader(jsonReader);
            fail("Should throw exception for malformed JSON in strict mode");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention strictness", 
                e.getMessage().contains("setStrictness(Strictness.LENIENT)"));
        }
    }

    @Test
    public void parseReader_afterReaderExhausted_returnsJsonNull() {
        StringReader stringReader = new StringReader("[\"GoyG.G\"]");
        // First parse exhausts the reader
        JsonParser.parseReader(stringReader);
        
        JsonReader jsonReader = new JsonReader(stringReader);
        JsonElement result = JsonParser.parseReader(jsonReader);
        
        assertTrue("Exhausted reader should return JsonNull", result.isJsonNull());
    }

    @Test(expected = NullPointerException.class)
    public void parseReader_withNullJsonReader_throwsNullPointerException() {
        JsonParser.parseReader((JsonReader) null);
    }

    @Test(expected = IllegalStateException.class)
    public void parseReader_calledTwiceOnSameJsonReader_throwsIllegalStateException() {
        StringReader stringReader = new StringReader("\"R3P?4\"");
        JsonReader jsonReader = new JsonReader(stringReader);
        
        // First call succeeds
        JsonParser.parseReader(jsonReader);
        // Second call should fail
        JsonParser.parseReader(jsonReader);
    }

    // ========== Deprecated Instance Method Tests ==========

    @Test
    public void deprecatedParse_withEmptyString_returnsJsonNull() {
        JsonParser parser = new JsonParser();
        
        JsonElement result = parser.parse("");
        
        assertTrue("Empty string should parse to JsonNull", result.isJsonNull());
    }

    @Test
    public void deprecatedParse_withSingleCharacter_returnsJsonPrimitive() {
        JsonParser parser = new JsonParser();
        
        JsonPrimitive result = (JsonPrimitive) parser.parse("\"N\"");
        
        assertFalse("Single character should not be a number", result.isNumber());
    }

    @Test
    public void deprecatedParse_withStringReader_returnsJsonPrimitive() {
        JsonParser parser = new JsonParser();
        StringReader reader = new StringReader("\"M(\"");
        
        JsonPrimitive result = (JsonPrimitive) parser.parse(reader);
        
        assertFalse("String primitive should not be boolean", result.isBoolean());
    }

    @Test
    public void deprecatedParse_withJsonReaderContainingObject_returnsJsonObject() {
        JsonParser parser = new JsonParser();
        StringReader stringReader = new StringReader("{}/GH6t)T:8sOB3 #");
        JsonReader jsonReader = new JsonReader(stringReader);
        
        JsonElement result = parser.parse(jsonReader);
        
        assertTrue("Result should be JsonObject", result.isJsonObject());
    }

    @Test
    public void deprecatedParse_withJsonArray_returnsJsonArray() {
        JsonParser parser = new JsonParser();
        String jsonArrayString = "[\"GoyG.G\"]";
        
        JsonArray result = (JsonArray) parser.parse(jsonArrayString);
        
        assertFalse("JsonArray should not be empty", result.isEmpty());
        assertTrue("Result should be JsonArray", result.isJsonArray());
    }

    @Test
    public void deprecatedParse_afterReaderExhausted_returnsJsonNull() {
        JsonParser parser = new JsonParser();
        StringReader stringReader = new StringReader("|h|[8&ZGQ1Fbg]Xp");
        JsonReader jsonReader = new JsonReader(stringReader);
        
        // First parse
        parser.parse(jsonReader);
        // Second parse on exhausted reader
        JsonElement result = parser.parse(jsonReader);
        
        assertFalse("Result should not be JsonObject", result.isJsonObject());
        assertTrue("Exhausted reader should return JsonNull", result.isJsonNull());
    }

    // ========== Error Cases for Deprecated Methods ==========

    @Test(expected = NullPointerException.class)
    public void deprecatedParse_withNullString_throwsNullPointerException() {
        JsonParser parser = new JsonParser();
        parser.parse((String) null);
    }

    @Test(expected = RuntimeException.class)
    public void deprecatedParse_withMalformedJsonString_throwsRuntimeException() {
        JsonParser parser = new JsonParser();
        parser.parse("Did not consume the entire document.");
    }

    @Test(expected = NullPointerException.class)
    public void deprecatedParse_withNullReader_throwsNullPointerException() {
        JsonParser parser = new JsonParser();
        parser.parse((Reader) null);
    }

    @Test(expected = RuntimeException.class)
    public void deprecatedParse_withInvalidJsonReader_throwsRuntimeException() {
        JsonParser parser = new JsonParser();
        StringReader reader = new StringReader("}Yz");
        
        parser.parse(reader);
    }

    @Test(expected = NullPointerException.class)
    public void deprecatedParse_withNullJsonReader_throwsNullPointerException() {
        JsonParser parser = new JsonParser();
        parser.parse((JsonReader) null);
    }

    @Test(expected = IllegalStateException.class)
    public void deprecatedParse_calledTwiceOnSameJsonReader_throwsIllegalStateException() {
        JsonParser parser = new JsonParser();
        StringReader stringReader = new StringReader("\"?3p\"");
        JsonReader jsonReader = new JsonReader(stringReader);
        
        // First call succeeds
        parser.parse(jsonReader);
        // Second call should fail
        parser.parse(jsonReader);
    }

    @Test(expected = RuntimeException.class)
    public void deprecatedParse_withInvalidJsonInJsonReader_throwsRuntimeException() {
        JsonParser parser = new JsonParser();
        StringReader stringReader = new StringReader("}pm");
        JsonReader jsonReader = new JsonReader(stringReader);
        
        parser.parse(jsonReader);
    }
}