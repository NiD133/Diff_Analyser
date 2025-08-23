package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.evosuite.runtime.mock.java.io.MockPrintWriter;
import org.junit.Test;

import java.io.StringWriter;

/**
 * Tests for {@link TypeAdapterRuntimeTypeWrapper}, focusing on serialization edge cases.
 */
public class TypeAdapterRuntimeTypeWrapper_ESTestTest3 {

    /**
     * Tests that the wrapper's write method fails when the object's runtime type
     * is more specific than the declared type, and Gson's default reflective adapter
     * for that runtime type cannot serialize it.
     *
     * <p>In this scenario, the declared type is {@code Object}, but the actual object
     * is a {@code MockPrintWriter}. The wrapper correctly looks up the adapter for
     * {@code MockPrintWriter.class}. However, Gson's attempt to use reflection to
     * serialize the complex internal state of a {@code PrintWriter} (which is not
     * a simple data object) results in a {@link StackOverflowError}. The test
     * confirms that this error is propagated as expected.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void write_withUnserializableRuntimeType_throwsStackOverflowError() {
        // Arrange
        Gson gson = new Gson();
        Class<Object> declaredType = Object.class;

        // Get the default adapter for Object, which will be wrapped.
        TypeAdapter<Object> objectTypeAdapter = gson.getAdapter(declaredType);
        TypeAdapterRuntimeTypeWrapper<Object> typeWrapper =
                new TypeAdapterRuntimeTypeWrapper<>(gson, objectTypeAdapter, declaredType);

        // Create an object whose runtime type is different from the declared type (Object).
        // A PrintWriter is not a simple data object and is not designed for reflective
        // serialization, which will cause a StackOverflowError.
        Object unserializableObject = new MockPrintWriter(new StringWriter());

        JsonWriter jsonWriter = new JsonWriter(new StringWriter());

        // Act & Assert
        // The write call will trigger the lookup of the runtime-specific adapter.
        // The reflective adapter for MockPrintWriter will then fail with a StackOverflowError.
        typeWrapper.write(jsonWriter, unserializableObject);
    }
}