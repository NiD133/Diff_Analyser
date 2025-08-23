package com.google.gson.internal.bind;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test focuses on the exception handling of the {@link TypeAdapterRuntimeTypeWrapper#read(JsonReader)} method.
 */
public class TypeAdapterRuntimeTypeWrapperTest {

    /**
     * Verifies that if the delegate TypeAdapter throws an IOException during reading,
     * the TypeAdapterRuntimeTypeWrapper correctly propagates this exception.
     *
     * The read() method's primary responsibility is to delegate the call, and this
     * test ensures it also delegates the failure condition transparently.
     */
    @Test
    public void read_whenDelegateThrowsIOException_propagatesException() throws IOException {
        // Arrange
        // 1. Create a mock for the delegate TypeAdapter. The wrapper's read() method
        //    is expected to simply delegate to this adapter.
        @SuppressWarnings("unchecked")
        TypeAdapter<Integer> mockDelegateAdapter = mock(TypeAdapter.class);

        // 2. Create a dummy JsonReader. Its content is irrelevant because the delegate is mocked.
        JsonReader dummyJsonReader = new JsonReader(new StringReader(""));

        // 3. Configure the mock delegate to throw a specific IOException when its read() method is called.
        IOException expectedException = new IOException("Simulated I/O failure from delegate");
        when(mockDelegateAdapter.read(dummyJsonReader)).thenThrow(expectedException);

        // 4. Instantiate the class under test, providing it with the mock delegate.
        //    The Gson context is null as it is not used by the read() method.
        TypeAdapter<Integer> wrapper = new TypeAdapterRuntimeTypeWrapper<>(null, mockDelegateAdapter, Integer.class);

        // Act & Assert
        // Verify that calling read() on the wrapper throws the expected IOException.
        IOException actualException = assertThrows(IOException.class, () -> {
            wrapper.read(dummyJsonReader);
        });

        // Ensure the propagated exception is the exact same instance thrown by the mock delegate.
        assertSame("The exception propagated by the wrapper should be the one from the delegate",
                expectedException, actualException);
    }
}