package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Tests for {@link TypeAdapterRuntimeTypeWrapper}.
 */
public class TypeAdapterRuntimeTypeWrapperTest {

    @Test
    public void read_whenReaderIsNull_throwsNullPointerException() {
        // Arrange
        Gson gson = new Gson();
        Class<Object> objectType = Object.class;

        // Get the delegate adapter that the wrapper will use. For Object.class, this is an ObjectTypeAdapter.
        TypeAdapter<Object> delegateAdapter = gson.getAdapter(objectType);
        TypeAdapter<Object> wrapperAdapter = new TypeAdapterRuntimeTypeWrapper<>(gson, delegateAdapter, objectType);

        // Act & Assert
        try {
            // The read method is expected to delegate to the underlying adapter,
            // which will throw a NullPointerException if the reader is null.
            wrapperAdapter.read(null);
            fail("A NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // The test passes if this exception is caught.
            // We can also assert on the message if it's part of the contract.
            assertNull("The NPE from the underlying adapter has no message.", expected.getMessage());
        }
    }
}