package com.google.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link TypeAdapter} class, focusing on specific utility methods.
 */
public class TypeAdapterTest {

    /**
     * Verifies that a null-safe adapter, when deserializing a {@link JsonNull} element
     * via {@link TypeAdapter#fromJsonTree(JsonElement)}, correctly returns a null object
     * without delegating to the original (wrapped) adapter.
     */
    @Test
    public void fromJsonTree_withNullSafeAdapterAndJsonNull_returnsNull() {
        // Arrange: Create a delegate adapter that fails if its read method is ever called.
        // This ensures the nullSafe() wrapper is handling the null case itself.
        TypeAdapter<Object> delegateAdapter = new TypeAdapter<Object>() {
            @Override
            public void write(JsonWriter out, Object value) {
                fail("write() should not be called on the delegate adapter.");
            }

            @Override
            public Object read(JsonReader in) {
                fail("read() should not be called by the nullSafe adapter for a null input.");
                return null; // Unreachable code
            }
        };

        // Create the null-safe adapter, which is the object under test.
        TypeAdapter<Object> nullSafeAdapter = delegateAdapter.nullSafe();
        JsonElement jsonNullInput = JsonNull.INSTANCE;

        // Act: Attempt to deserialize a JsonNull instance.
        Object result = nullSafeAdapter.fromJsonTree(jsonNullInput);

        // Assert: The result should be null, and the test should pass without the delegate's
        // read() method being called (which would have triggered a fail()).
        assertNull("The result of deserializing JsonNull should be a null object.", result);
    }
}