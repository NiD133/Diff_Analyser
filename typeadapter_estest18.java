package com.google.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for the {@link TypeAdapter} class, focusing on specific behaviors
 * like null safety.
 */
public class TypeAdapterTest {

    /**
     * Verifies that calling {@code fromJsonTree} on a null-safe adapter with a null
     * {@code JsonElement} throws a {@code NullPointerException}.
     *
     * <p>This behavior occurs because {@code fromJsonTree} internally creates a
     * {@code JsonTreeReader} which cannot process a null element. When the null-safe
     * wrapper then attempts to read from this reader, it fails.
     */
    @Test(expected = NullPointerException.class)
    public void fromJsonTree_withNullElementOnNullSafeAdapter_throwsNullPointerException() {
        // Arrange: Create a basic TypeAdapter and wrap it with the nullSafe() adapter.
        // The specific implementation of the base adapter is not relevant for this test.
        TypeAdapter<Object> dummyAdapter = new TypeAdapter<Object>() {
            @Override
            public void write(JsonWriter out, Object value) throws IOException {
                // Not used in this test.
            }

            @Override
            public Object read(JsonReader in) throws IOException {
                // Not used in this test, but must be implemented.
                in.skipValue();
                return null;
            }
        };
        TypeAdapter<Object> nullSafeAdapter = dummyAdapter.nullSafe();

        // Act: Attempt to deserialize a null JsonElement.
        // This is expected to throw a NullPointerException.
        nullSafeAdapter.fromJsonTree(null);

        // Assert: The expected exception is verified by the @Test annotation.
    }
}