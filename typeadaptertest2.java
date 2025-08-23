package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for the {@link TypeAdapter#nullSafe()} method.
 */
public class TypeAdapterNullSafeTest {

    /**
     * A base adapter whose read/write methods are not meant to be called.
     * This is used to verify that the {@link TypeAdapter#nullSafe()} wrapper
     * is being tested, not the underlying adapter's logic.
     */
    private static final TypeAdapter<String> baseAdapter = new TypeAdapter<>() {
        @Override
        public void write(JsonWriter out, String value) {
            throw new AssertionError("baseAdapter.write should not be called");
        }

        @Override
        public String read(JsonReader in) {
            throw new AssertionError("baseAdapter.read should not be called");
        }
    };

    @Test
    public void nullSafe_isIdempotent() {
        // Arrange: Create a null-safe adapter by wrapping a base adapter.
        TypeAdapter<String> nullSafeAdapter = baseAdapter.nullSafe();

        // Act & Assert: Calling nullSafe() again on an already null-safe adapter
        // should return the exact same instance, not a new wrapper.
        assertThat(nullSafeAdapter.nullSafe()).isSameInstanceAs(nullSafeAdapter);
    }
}