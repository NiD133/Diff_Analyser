package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Tests for {@link TypeAdapterRuntimeTypeWrapper}.
 */
public class TypeAdapterRuntimeTypeWrapperTest {

    /**
     * This test verifies that the {@code write} method throws a NullPointerException
     * when the provided JsonWriter is null.
     *
     * The test sets up the wrapper with a declared type (Number) that is different
     * from the runtime type of the value being written (Integer). This forces the
     * wrapper to look up the more specific adapter from the Gson context. It is this
     * downstream adapter that is expected to throw the NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void write_withNullJsonWriter_throwsNullPointerException() throws IOException {
        // Arrange
        Gson gson = new Gson();

        // The delegate adapter is mocked, as its behavior is not relevant for this test.
        // The wrapper will choose a different, more specific adapter at runtime.
        @SuppressWarnings("unchecked")
        TypeAdapter<Object> mockDelegateAdapter = mock(TypeAdapter.class);

        // Use a generic supertype (Number) as the declared type.
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(
            gson,
            mockDelegateAdapter,
            Number.class
        );

        // The value to be written has a more specific runtime type (Integer).
        Object valueToWrite = 123;

        // Act & Assert
        // The wrapper will resolve the runtime type of 'valueToWrite' as Integer,
        // get the corresponding adapter from Gson, and then call its write method.
        // That downstream adapter is expected to throw a NullPointerException
        // when given a null JsonWriter.
        wrapper.write(null, valueToWrite);
    }
}