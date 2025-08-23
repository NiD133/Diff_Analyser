package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TypeAdapterRuntimeTypeWrapperTest {

    /**
     * A simple class without a registered type adapter.
     * Gson's default reflective adapter will be used for it.
     * It contains a field of type {@link Class} which, by default, Gson cannot serialize.
     */
    private static class ClassWithUnsupportedField {
        @SuppressWarnings("unused")
        private final Class<?> unsupportedField = Integer.class;
    }

    @Test
    public void write_whenRuntimeTypeCannotBeSerialized_throwsException() throws IOException {
        // Arrange
        Gson gson = new Gson();

        // A delegate adapter that should not be used, because the runtime type of the value
        // is different from the declared type for which the wrapper is created.
        TypeAdapter<Object> delegate = new TypeAdapter<Object>() {
            @Override
            public void write(JsonWriter out, Object value) {
                fail("Delegate adapter should not have been called");
            }
            @Override
            public Object read(JsonReader in) {
                fail("Delegate adapter should not have been called");
                return null;
            }
        };

        // The wrapper is created for the general `Object` type.
        TypeAdapter<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, delegate, Object.class);

        // The actual value has a more specific runtime type (`ClassWithUnsupportedField`)
        // for which Gson will use a reflective adapter.
        Object valueToSerialize = new ClassWithUnsupportedField();
        JsonWriter jsonWriter = new JsonWriter(new StringWriter());

        // Act & Assert
        try {
            wrapper.write(jsonWriter, valueToSerialize);
            fail("Expected an UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // This exception is expected. It is thrown by Gson's default adapter for `java.lang.Class`
            // when it tries to reflectively serialize the `unsupportedField`.
            String expectedMessage = "Attempted to serialize java.lang.Class: java.lang.Integer";
            assertTrue(
                "Exception message should indicate failure to serialize Class object. Actual: " + e.getMessage(),
                e.getMessage().contains(expectedMessage)
            );
        }
    }
}