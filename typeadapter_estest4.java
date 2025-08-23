package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link TypeAdapter} class, focusing on the null-safe wrapper.
 */
public class TypeAdapterTest {

    @Test
    public void nullSafeRead_shouldReturnNull_whenJsonValueIsNull() throws IOException {
        // Arrange
        // 1. Create a delegate TypeAdapter. The specific implementation (FutureTypeAdapter)
        //    is not important; we just need an instance to wrap.
        TypeAdapter<Object> delegateAdapter = new Gson.FutureTypeAdapter<>();

        // 2. Create the null-safe wrapper, which is the object under test.
        TypeAdapter<Object> nullSafeAdapter = delegateAdapter.nullSafe();

        // 3. Prepare a JSON reader that will provide the JSON literal "null".
        JsonReader jsonReader = new JsonReader(new StringReader("null"));

        // Act
        // Attempt to read the value using the null-safe adapter.
        Object result = nullSafeAdapter.read(jsonReader);

        // Assert
        // Verify that the null-safe adapter correctly handles the JSON null
        // by returning a Java null, without delegating to the original adapter.
        assertNull(result);
    }
}