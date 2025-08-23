package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link TypeAdapterRuntimeTypeWrapper}, focusing on its delegation behavior.
 */
public class TypeAdapterRuntimeTypeWrapperTest {

    /**
     * Verifies that when {@code write()} is called with a null {@link JsonWriter},
     * the wrapper correctly delegates the call to the underlying {@link TypeAdapter}.
     * The underlying adapter is then expected to throw a {@link NullPointerException}.
     *
     * <p>This test specifically checks that the exception originates from the
     * delegate adapter (in this case, {@code ObjectTypeAdapter}), confirming the
     * delegation mechanism works as intended.
     */
    @Test
    public void write_withNullJsonWriter_throwsNpeFromDelegateAdapter() throws Exception {
        // Arrange
        Gson gson = new Gson();
        Class<Object> objectType = Object.class;

        // For Object.class, Gson's default adapter is ObjectTypeAdapter.
        TypeAdapter<Object> delegateAdapter = gson.getAdapter(objectType);
        TypeAdapter<Object> typeAdapterWrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, delegateAdapter, objectType);

        // Act & Assert
        try {
            // Attempt to write a null value with a null writer.
            // The wrapper should delegate to ObjectTypeAdapter, which will then throw
            // a NullPointerException upon trying to use the null writer.
            typeAdapterWrapper.write(null, null);
            fail("A NullPointerException was expected but not thrown.");
        } catch (NullPointerException e) {
            // Verify that the exception was thrown by the delegate adapter, as expected.
            StackTraceElement topOfStack = e.getStackTrace()[0];
            assertEquals("com.google.gson.internal.bind.ObjectTypeAdapter", topOfStack.getClassName());
        }
    }
}