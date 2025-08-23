package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.lang.reflect.Type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

// The original test class name is kept for context.
public class TypeAdapterRuntimeTypeWrapper_ESTestTest12 {

    /**
     * Tests that the write() method throws a NullPointerException when given a null JsonWriter,
     * specifically when the runtime type of the value differs from the declared type,
     * forcing the wrapper to delegate to a new adapter looked up from the Gson context.
     */
    @Test
    public void write_withNullJsonWriterAndMismatchedType_throwsNullPointerException() throws Exception {
        // Arrange
        Gson gson = new Gson();

        // Create a mock delegate adapter. We expect this adapter to be bypassed.
        @SuppressWarnings("unchecked")
        TypeAdapter<Object> mockDelegateAdapter = mock(TypeAdapter.class);

        // Use a declared type (Short) that will not match the runtime type of the value (TypeToken).
        // This forces the wrapper to find a more specific adapter at runtime.
        Type declaredType = Short.TYPE;

        TypeAdapterRuntimeTypeWrapper<Object> typeAdapterWrapper =
            new TypeAdapterRuntimeTypeWrapper<>(gson, mockDelegateAdapter, declaredType);

        // The value to be written. Its runtime type (TypeToken) is different from the
        // declaredType, triggering the wrapper's primary logic. Gson's default adapter
        // for TypeToken is a reflective adapter, which is the one we want to test.
        Object valueToWrite = TypeToken.get(String.class);

        // Act & Assert
        try {
            // The wrapper will look up the adapter for TypeToken.class, which is a reflective
            // adapter. That adapter will then fail when trying to use the null writer.
            typeAdapterWrapper.write(null, valueToWrite);
            fail("A NullPointerException was expected but not thrown.");
        } catch (NullPointerException e) {
            // This is the expected behavior. The underlying adapter (in this case,
            // Gson's reflective adapter) does not handle a null JsonWriter.
            assertNotNull("The thrown exception should not be null.", e);
        }

        // Verify that the original delegate adapter was never called, confirming that the
        // wrapper correctly chose a different adapter based on the runtime type.
        verify(mockDelegateAdapter, never()).write(any(JsonWriter.class), any());
    }
}