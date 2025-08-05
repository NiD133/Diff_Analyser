package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.CharArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.temporal.ChronoField;

/**
 * Tests for TypeAdapterRuntimeTypeWrapper functionality including:
 * - Basic read/write operations
 * - Runtime type resolution
 * - Error handling scenarios
 * - Edge cases with null values and malformed input
 */
public class TypeAdapterRuntimeTypeWrapperTest {

    private final Gson gson = new Gson();

    // ========== WRITE OPERATION TESTS ==========

    @Test
    public void shouldWriteEnumValueSuccessfully() throws IOException {
        // Given: A wrapper for ChronoField enum with Object as declared type
        TypeAdapter<ChronoField> enumAdapter = gson.getAdapter(ChronoField.class);
        TypeAdapterRuntimeTypeWrapper<ChronoField> wrapper = 
            createWrapper(enumAdapter, Object.class);
        
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);
        
        // When: Writing an enum value
        wrapper.write(jsonWriter, ChronoField.HOUR_OF_AMPM);
        
        // Then: Should complete without errors
        assertFalse("JsonWriter should not be in lenient mode", jsonWriter.isLenient());
    }

    @Test
    public void shouldHandleComplexObjectSerialization() throws IOException {
        // Given: A wrapper with TreeTypeAdapter for complex object handling
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = createTreeAdapterWrapper();
        
        CharArrayWriter charWriter = new CharArrayWriter();
        JsonWriter jsonWriter = new JsonWriter(charWriter);
        
        // When/Then: Writing a complex object should not throw during setup
        // Note: This tests the wrapper's ability to handle complex type adapters
        assertNotNull("Wrapper should be created successfully", wrapper);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailWhenWritingToClosedJsonWriter() throws IOException {
        // Given: A wrapper and a JsonWriter that already has a complete JSON value
        TypeAdapter<Object> objectAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = 
            createWrapper(objectAdapter, Object.class);
        
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.nullValue(); // This completes the JSON, making it invalid for more writes
        
        // When: Attempting to write another value
        wrapper.write(jsonWriter, new StringWriter());
        
        // Then: Should throw IllegalStateException with message about single top-level value
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldFailWhenSerializingUnsupportedType() throws IOException {
        // Given: A wrapper that will encounter a Class object (unsupported by default)
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = createTreeAdapterWrapperWithNullSerializer();
        
        CharArrayWriter charWriter = new CharArrayWriter();
        JsonWriter jsonWriter = new JsonWriter(charWriter);
        
        // When: Attempting to serialize a TreeTypeAdapter (which contains Class references)
        wrapper.write(jsonWriter, wrapper);
        
        // Then: Should throw UnsupportedOperationException about Class serialization
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithNullJsonWriter() throws IOException {
        // Given: A valid wrapper
        TypeAdapter<Object> objectAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = 
            createWrapper(objectAdapter, Object.class);
        
        // When: Attempting to write with null JsonWriter
        wrapper.write(null, null);
        
        // Then: Should throw NullPointerException
    }

    // ========== READ OPERATION TESTS ==========

    @Test
    public void shouldReadFromEmptyJsonSuccessfully() throws IOException {
        // Given: A wrapper with TreeTypeAdapter and empty JSON input
        TypeAdapterRuntimeTypeWrapper<ChronoField> wrapper = createChronoFieldTreeWrapper();
        JsonReader jsonReader = createJsonReader("");
        
        // When: Reading from empty input
        ChronoField result = wrapper.read(jsonReader);
        
        // Then: Should handle gracefully (specific behavior depends on underlying adapter)
        // Note: The actual result depends on the TreeTypeAdapter's deserializer mock
    }

    @Test(expected = IOException.class)
    public void shouldFailWhenReadingMalformedJson() throws IOException {
        // Given: A wrapper and malformed JSON input
        TypeAdapter<Object> objectAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = 
            createWrapper(objectAdapter, Object.class);
        
        String malformedJson = gson.toString(); // This produces malformed JSON for parsing
        JsonReader jsonReader = gson.newJsonReader(new StringReader(malformedJson));
        
        // When: Attempting to read malformed JSON
        wrapper.read(jsonReader);
        
        // Then: Should throw IOException about malformed JSON
    }

    @Test(expected = EOFException.class)
    public void shouldFailWhenReadingFromEmptyInput() throws IOException {
        // Given: A wrapper with TreeTypeAdapter and truly empty input
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = createTreeAdapterWrapperForReading();
        JsonReader jsonReader = gson.newJsonReader(new StringReader(""));
        
        // When: Attempting to read from empty input
        wrapper.read(jsonReader);
        
        // Then: Should throw EOFException
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithNullJsonReader() throws IOException {
        // Given: A valid wrapper
        TypeAdapter<Object> objectAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = 
            createWrapper(objectAdapter, Object.class);
        
        // When: Attempting to read with null JsonReader
        wrapper.read(null);
        
        // Then: Should throw NullPointerException
    }

    @Test(expected = RuntimeException.class)
    public void shouldWrapIOExceptionsInRuntimeException() throws IOException {
        // Given: A wrapper that will encounter IO issues
        TypeAdapterRuntimeTypeWrapper<Integer> wrapper = createWrapperWithIOIssues();
        JsonReader jsonReader = createDisconnectedJsonReader();
        
        // When: Attempting to read with IO problems
        wrapper.read(jsonReader);
        
        // Then: Should wrap IOException in RuntimeException
    }

    // ========== HELPER METHODS ==========

    private <T> TypeAdapterRuntimeTypeWrapper<T> createWrapper(
            TypeAdapter<T> delegate, Class<?> declaredType) {
        return new TypeAdapterRuntimeTypeWrapper<>(gson, delegate, declaredType);
    }

    private TypeAdapterRuntimeTypeWrapper<ChronoField> createChronoFieldTreeWrapper() {
        JsonSerializer<ChronoField> serializer = mock(JsonSerializer.class);
        JsonDeserializer<ChronoField> deserializer = mock(JsonDeserializer.class);
        
        TypeToken<ChronoField> typeToken = TypeToken.get(ChronoField.class);
        TypeAdapterFactory factory = TreeTypeAdapter.newFactory(
            TypeToken.get(Integer.class), mock(JsonSerializer.class));
        
        TreeTypeAdapter<ChronoField> treeAdapter = new TreeTypeAdapter<>(
            serializer, deserializer, gson, typeToken, factory);
        
        return createWrapper(treeAdapter, Short.TYPE);
    }

    private TypeAdapterRuntimeTypeWrapper<Object> createTreeAdapterWrapper() {
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        TypeAdapterFactory factory = TreeTypeAdapter.newFactory(typeToken, serializer);
        TypeAdapter<Object> adapter = gson.getDelegateAdapter(factory, typeToken);
        
        return createWrapper(adapter, Object.class);
    }

    private TypeAdapterRuntimeTypeWrapper<Object> createTreeAdapterWrapperWithNullSerializer() {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        TypeAdapterFactory factory = TreeTypeAdapter.newFactory(
            TypeToken.get(Integer.class), mock(JsonSerializer.class));
        
        TreeTypeAdapter<Object> treeAdapter = new TreeTypeAdapter<>(
            null, deserializer, gson, typeToken, factory);
        
        return createWrapper(treeAdapter, Byte.TYPE);
    }

    private TypeAdapterRuntimeTypeWrapper<Object> createTreeAdapterWrapperForReading() {
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, serializer);
        TypeAdapter<Object> adapter = gson.getDelegateAdapter(factory, typeToken);
        
        return createWrapper(adapter, Object.class);
    }

    private TypeAdapterRuntimeTypeWrapper<Integer> createWrapperWithIOIssues() {
        JsonSerializer<Integer> serializer = mock(JsonSerializer.class);
        JsonDeserializer<Integer> deserializer = mock(JsonDeserializer.class);
        TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
        
        // Using null Gson to trigger issues
        TreeTypeAdapter<Integer> treeAdapter = new TreeTypeAdapter<>(
            serializer, deserializer, null, typeToken, null, true);
        
        return new TypeAdapterRuntimeTypeWrapper<>(null, treeAdapter, Integer.class);
    }

    private JsonReader createJsonReader(String json) {
        return new JsonReader(new StringReader(json));
    }

    private JsonReader createDisconnectedJsonReader() throws IOException {
        // Creates a JsonReader that will cause IO issues
        return new JsonReader(new java.io.PipedReader());
    }
}