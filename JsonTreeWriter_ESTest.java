package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * Test suite for JsonTreeWriter functionality.
 * Tests JSON tree construction, configuration options, and error handling.
 */
public class JsonTreeWriterTest {

    // ========== Basic JSON Structure Tests ==========
    
    @Test
    public void shouldCreateEmptyJsonNull_WhenNoContentWritten() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        JsonElement result = writer.get();
        
        assertTrue("Expected JsonNull when no content written", result.isJsonNull());
    }

    @Test
    public void shouldCreateJsonPrimitive_WhenWritingSimpleValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.value(495.5255);
        JsonPrimitive result = (JsonPrimitive) writer.get();
        
        assertTrue("Expected numeric primitive", result.isNumber());
        assertEquals(495.5255, result.getAsDouble(), 0.001);
    }

    @Test
    public void shouldCreateJsonObject_WhenWritingObjectStructure() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.endObject();
        JsonObject result = (JsonObject) writer.get();
        
        assertTrue("Expected JsonObject", result.isJsonObject());
        assertEquals("Expected empty object", 0, result.size());
    }

    @Test
    public void shouldCreateJsonArray_WhenWritingArrayStructure() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginArray();
        writer.endArray();
        JsonArray result = (JsonArray) writer.get();
        
        assertTrue("Expected JsonArray", result.isJsonArray());
        assertEquals("Expected empty array", 0, result.size());
    }

    // ========== Nested Structure Tests ==========
    
    @Test
    public void shouldCreateNestedArray_WhenWritingArrayInsideArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginArray();
        writer.beginArray();
        writer.endArray();
        writer.endArray();
        
        JsonArray result = (JsonArray) writer.get();
        assertEquals("Expected outer array with one element", 1, result.size());
        assertTrue("Expected nested array", result.get(0).isJsonArray());
    }

    @Test
    public void shouldCreateObjectWithProperty_WhenWritingNameValuePair() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.name("testProperty");
        writer.value("testValue");
        writer.endObject();
        
        JsonObject result = (JsonObject) writer.get();
        assertEquals("Expected object with one property", 1, result.size());
        assertEquals("testValue", result.get("testProperty").getAsString());
    }

    // ========== Value Type Tests ==========
    
    @Test
    public void shouldWriteBooleanValues() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.value(true);
        JsonPrimitive result = (JsonPrimitive) writer.get();
        
        assertTrue("Expected boolean primitive", result.isBoolean());
        assertTrue("Expected true value", result.getAsBoolean());
    }

    @Test
    public void shouldWriteStringValues() {
        JsonTreeWriter writer = new JsonTreeWriter();
        String testString = "Hello, World!";
        
        writer.value(testString);
        JsonPrimitive result = (JsonPrimitive) writer.get();
        
        assertTrue("Expected string primitive", result.isString());
        assertEquals(testString, result.getAsString());
    }

    @Test
    public void shouldWriteNumericValues_ForDifferentNumberTypes() {
        // Test long
        JsonTreeWriter longWriter = new JsonTreeWriter();
        longWriter.value(123L);
        assertEquals(123L, ((JsonPrimitive) longWriter.get()).getAsLong());

        // Test double
        JsonTreeWriter doubleWriter = new JsonTreeWriter();
        doubleWriter.value(123.45);
        assertEquals(123.45, ((JsonPrimitive) doubleWriter.get()).getAsDouble(), 0.001);

        // Test float
        JsonTreeWriter floatWriter = new JsonTreeWriter();
        floatWriter.value(123.45f);
        assertEquals(123.45f, ((JsonPrimitive) floatWriter.get()).getAsFloat(), 0.001);
    }

    @Test
    public void shouldWriteNullValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.nullValue();
        JsonElement result = writer.get();
        
        assertTrue("Expected JsonNull", result.isJsonNull());
    }

    @Test
    public void shouldHandleNullStringValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.value((String) null);
        JsonElement result = writer.get();
        
        assertTrue("Expected JsonNull for null string", result.isJsonNull());
    }

    // ========== Configuration Tests ==========
    
    @Test
    public void shouldMaintainLenientStrictness_WhenConfigured() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.setStrictness(Strictness.LENIENT);
        writer.value("test");
        
        assertTrue("Expected lenient mode", writer.isLenient());
    }

    @Test
    public void shouldMaintainHtmlSafeMode_WhenConfigured() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.setHtmlSafe(true);
        writer.value("test");
        
        assertTrue("Expected HTML safe mode", writer.isHtmlSafe());
    }

    @Test
    public void shouldMaintainSerializeNullsMode_WhenConfigured() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.setSerializeNulls(false);
        writer.value("test");
        
        assertFalse("Expected serialize nulls disabled", writer.getSerializeNulls());
    }

    // ========== Error Handling Tests ==========
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowException_WhenWritingValueInObjectWithoutName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.value("unexpected value"); // Should fail - no name specified
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowException_WhenWritingNameInArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginArray();
        writer.name("unexpected name"); // Should fail - names not allowed in arrays
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowException_WhenWritingNameAtRootLevel() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.name("unexpected name"); // Should fail - not in an object
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowException_WhenNameIsNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.name(null); // Should fail - null name not allowed
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowException_WhenEndingWrongStructureType() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginArray();
        writer.endObject(); // Should fail - mismatched begin/end
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowException_WhenEndingWithoutBeginning() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.endObject(); // Should fail - no matching begin
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowException_WhenObjectHasUnfinishedProperty() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.name("property");
        writer.endObject(); // Should fail - property has no value
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowException_WhenWritingAfterClose() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.close();
        writer.beginObject(); // Should fail - writer is closed
    }

    @Test
    public void shouldThrowIOException_WhenClosingIncompleteDocument() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginArray();
        
        try {
            writer.close();
            fail("Expected IOException for incomplete document");
        } catch (IOException e) {
            assertEquals("Incomplete document", e.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowException_WhenGettingIncompleteResult() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginArray();
        writer.get(); // Should fail - array not completed
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowException_WhenUsingJsonValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.jsonValue("{}"); // Should fail - not supported
    }

    // ========== Utility Tests ==========
    
    @Test
    public void shouldAllowFlush_WithoutSideEffects() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.flush(); // Should not throw exception
        
        assertEquals("Flush should not change strictness", 
                    Strictness.LEGACY_STRICT, writer.getStrictness());
    }

    @Test
    public void shouldAllowSuccessfulClose_WhenDocumentComplete() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.value("complete");
        writer.close(); // Should not throw exception
        
        // Verify document is accessible after close
        JsonElement result = writer.get();
        assertEquals("complete", result.getAsString());
    }

    // ========== Complex Structure Test ==========
    
    @Test
    public void shouldCreateComplexNestedStructure() {
        JsonTreeWriter writer = new JsonTreeWriter();
        
        writer.beginObject();
        writer.name("array");
        writer.beginArray();
        writer.value(1);
        writer.value(2);
        writer.beginObject();
        writer.name("nested");
        writer.value("value");
        writer.endObject();
        writer.endArray();
        writer.name("simple");
        writer.value("test");
        writer.endObject();
        
        JsonObject result = (JsonObject) writer.get();
        
        assertEquals("Expected 2 properties", 2, result.size());
        assertTrue("Expected array property", result.get("array").isJsonArray());
        assertEquals("Expected simple property", "test", result.get("simple").getAsString());
        
        JsonArray array = result.getAsJsonArray("array");
        assertEquals("Expected 3 array elements", 3, array.size());
        assertEquals(1, array.get(0).getAsInt());
        assertEquals(2, array.get(1).getAsInt());
        assertTrue("Expected nested object", array.get(2).isJsonObject());
    }
}