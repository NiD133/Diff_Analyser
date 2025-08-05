package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Test suite for TypeAdapter functionality, focusing on:
 * - Null-safe adapter behavior
 * - FutureTypeAdapter cyclic dependency handling
 * - JSON serialization/deserialization edge cases
 * - Error handling for malformed JSON and invalid states
 */
public class TypeAdapterTest {

    // ========== NULL-SAFE ADAPTER TESTS ==========
    
    @Test
    public void nullSafeAdapter_shouldSerializeNullToJsonString() throws Throwable {
        // Given: A null-safe type adapter
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        StringWriter output = new StringWriter();
        
        // When: Serializing null value to JSON
        nullSafeAdapter.toJson(output, null);
        
        // Then: Should produce "null" string
        assertEquals("null", output.toString());
    }

    @Test
    public void nullSafeAdapter_shouldConvertNullToJsonNullElement() throws Throwable {
        // Given: A null-safe type adapter for Integer
        TypeAdapter<Integer> nullSafeAdapter = createNullSafeIntegerAdapter();
        
        // When: Converting null to JsonElement
        JsonElement result = nullSafeAdapter.toJsonTree(null);
        
        // Then: Should return JsonNull (not a primitive)
        assertFalse(result.isJsonPrimitive());
        assertTrue(result.isJsonNull());
    }

    @Test
    public void nullSafeAdapter_shouldReturnNullStringForNullValue() throws Throwable {
        // Given: A null-safe type adapter
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        
        // When: Converting null to JSON string
        String result = nullSafeAdapter.toJson(null);
        
        // Then: Should return "null"
        assertEquals("null", result);
    }

    @Test
    public void nullSafeAdapter_shouldDeserializeJsonNullToNull() throws Throwable {
        // Given: A null-safe adapter and JSON null input
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        JsonReader jsonReader = createJsonReader("null");
        
        // When: Reading null from JSON
        Object result = nullSafeAdapter.read(jsonReader);
        
        // Then: Should return null
        assertNull(result);
    }

    @Test
    public void nullSafeAdapter_shouldConvertJsonNullElementToNull() throws Throwable {
        // Given: A null-safe adapter and JsonNull element
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        JsonNull jsonNull = JsonNull.INSTANCE;
        
        // When: Converting JsonNull to Java object
        Object result = nullSafeAdapter.fromJsonTree(jsonNull);
        
        // Then: Should return null
        assertNull(result);
    }

    @Test
    public void nullSafeAdapter_shouldDeserializeNullFromReader() throws Throwable {
        // Given: A null-safe adapter and reader with "null"
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        StringReader reader = new StringReader("null");
        
        // When: Deserializing from reader
        Object result = nullSafeAdapter.fromJson(reader);
        
        // Then: Should return null
        assertNull(result);
    }

    @Test
    public void nullSafeAdapter_shouldDeserializeNullFromString() throws Throwable {
        // Given: A null-safe adapter
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        
        // When: Deserializing "null" string
        Object result = nullSafeAdapter.fromJson("null");
        
        // Then: Should return null
        assertNull(result);
    }

    @Test
    public void nullSafe_shouldReturnSameInstanceWhenAlreadyNullSafe() throws Throwable {
        // Given: A null-safe adapter
        TypeAdapter<Integer> nullSafeAdapter = createNullSafeIntegerAdapter();
        
        // When: Calling nullSafe() again
        TypeAdapter<Integer> result = nullSafeAdapter.nullSafe();
        
        // Then: Should return the same instance (idempotent)
        assertSame(result, nullSafeAdapter);
    }

    // ========== FUTURE TYPE ADAPTER CYCLIC DEPENDENCY TESTS ==========

    @Test(expected = IllegalStateException.class)
    public void futureTypeAdapter_shouldThrowExceptionWhenUsedBeforeDelegateSet() throws Throwable {
        // Given: An unresolved FutureTypeAdapter
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        
        // When: Attempting to write before delegate is set
        // Then: Should throw IllegalStateException with cyclic dependency message
        futureAdapter.write(null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void futureTypeAdapter_shouldThrowExceptionWhenReadingBeforeDelegateSet() throws Throwable {
        // Given: An unresolved FutureTypeAdapter
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        
        // When: Attempting to read before delegate is set
        // Then: Should throw IllegalStateException
        futureAdapter.read(null);
    }

    @Test(expected = IllegalStateException.class)
    public void futureTypeAdapter_shouldThrowExceptionForSelfReference() throws Throwable {
        // Given: A FutureTypeAdapter that references itself (cyclic dependency)
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        futureAdapter.setDelegate(futureAdapter);
        
        // When: Attempting to serialize
        // Then: Should result in stack overflow or infinite recursion
        futureAdapter.toJson(futureAdapter);
    }

    @Test(expected = IllegalStateException.class)
    public void futureTypeAdapter_shouldThrowExceptionForCircularReference() throws Throwable {
        // Given: Two FutureTypeAdapters referencing each other
        Gson.FutureTypeAdapter<Object> adapter1 = new Gson.FutureTypeAdapter<>();
        Gson.FutureTypeAdapter<Object> adapter2 = new Gson.FutureTypeAdapter<>();
        adapter1.setDelegate(adapter2);
        adapter2.setDelegate(adapter1);
        
        // When: Attempting to serialize
        // Then: Should result in infinite recursion
        adapter1.toJsonTree(adapter1);
    }

    // ========== ERROR HANDLING TESTS ==========

    @Test(expected = NullPointerException.class)
    public void typeAdapter_shouldThrowExceptionForNullWriter() throws Throwable {
        // Given: An unresolved FutureTypeAdapter
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        
        // When: Attempting to write to null writer
        // Then: Should throw NullPointerException
        futureAdapter.toJson((Writer) null, null);
    }

    @Test(expected = NullPointerException.class)
    public void typeAdapter_shouldThrowExceptionForNullReader() throws Throwable {
        // Given: An unresolved FutureTypeAdapter
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        
        // When: Attempting to read from null reader
        // Then: Should throw NullPointerException
        futureAdapter.fromJson((Reader) null);
    }

    @Test(expected = NullPointerException.class)
    public void typeAdapter_shouldThrowExceptionForNullString() throws Throwable {
        // Given: An unresolved FutureTypeAdapter
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        
        // When: Attempting to parse null string
        // Then: Should throw NullPointerException
        futureAdapter.fromJson((String) null);
    }

    @Test(expected = IOException.class)
    public void typeAdapter_shouldThrowExceptionForMalformedJson() throws Throwable {
        // Given: A null-safe adapter and malformed JSON
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        
        // When: Attempting to parse malformed JSON
        // Then: Should throw IOException with strictness message
        nullSafeAdapter.fromJson("MZ8_invalid_json");
    }

    @Test(expected = EOFException.class)
    public void typeAdapter_shouldThrowExceptionForEmptyInput() throws Throwable {
        // Given: A null-safe adapter and empty input
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        
        // When: Attempting to parse empty string
        // Then: Should throw EOFException
        nullSafeAdapter.fromJson("");
    }

    @Test(expected = IOException.class)
    public void typeAdapter_shouldThrowExceptionForClosedReader() throws Throwable {
        // Given: A null-safe adapter and closed reader
        TypeAdapter<Integer> nullSafeAdapter = createNullSafeIntegerAdapter();
        StringReader reader = new StringReader("");
        reader.close();
        
        // When: Attempting to read from closed reader
        // Then: Should throw IOException
        nullSafeAdapter.fromJson(reader);
    }

    // ========== NULL-SAFE ADAPTER ERROR HANDLING ==========

    @Test(expected = NullPointerException.class)
    public void nullSafeAdapter_shouldThrowExceptionForNullJsonWriter() throws Throwable {
        // Given: A null-safe adapter
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        
        // When: Attempting to write with null JsonWriter
        // Then: Should throw NullPointerException
        nullSafeAdapter.write(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullSafeAdapter_shouldThrowExceptionForNullJsonReader() throws Throwable {
        // Given: A null-safe adapter
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        
        // When: Attempting to read with null JsonReader
        // Then: Should throw NullPointerException
        nullSafeAdapter.read(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullSafeAdapter_shouldThrowExceptionForNullJsonElement() throws Throwable {
        // Given: A null-safe adapter
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        
        // When: Attempting to convert null JsonElement
        // Then: Should throw NullPointerException
        nullSafeAdapter.fromJsonTree(null);
    }

    @Test(expected = IllegalStateException.class)
    public void nullSafeAdapter_shouldThrowExceptionWhenWrappingUnresolvedFutureAdapter() throws Throwable {
        // Given: A null-safe wrapper around unresolved FutureTypeAdapter and non-null JsonElement
        TypeAdapter<Object> nullSafeAdapter = createNullSafeAdapter();
        JsonPrimitive jsonPrimitive = new JsonPrimitive('B');
        
        // When: Attempting to convert JsonPrimitive
        // Then: Should throw IllegalStateException from underlying FutureTypeAdapter
        nullSafeAdapter.fromJsonTree(jsonPrimitive);
    }

    // ========== HELPER METHODS ==========

    private TypeAdapter<Object> createNullSafeAdapter() {
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        return futureAdapter.nullSafe();
    }

    private TypeAdapter<Integer> createNullSafeIntegerAdapter() {
        Gson.FutureTypeAdapter<Integer> futureAdapter = new Gson.FutureTypeAdapter<>();
        return futureAdapter.nullSafe();
    }

    private JsonReader createJsonReader(String json) {
        return new JsonReader(new StringReader(json));
    }
}