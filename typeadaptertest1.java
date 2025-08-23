package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for the {@link TypeAdapter#nullSafe()} wrapper.
 */
public class TypeAdapterNullSafeTest {

    /**
     * A test-only TypeAdapter that throws an {@link AssertionError} if its read or write
     * methods are called. This is used to verify that the {@link TypeAdapter#nullSafe()}
     * wrapper correctly handles nulls without delegating to the wrapped adapter.
     */
    private static final TypeAdapter<String> FAILING_ADAPTER = new TypeAdapter<>() {
        @Override
        public void write(JsonWriter out, String value) {
            throw new AssertionError("Wrapped adapter should not be called for null value");
        }

        @Override
        public String read(JsonReader in) {
            throw new AssertionError("Wrapped adapter should not be called for null value");
        }
    };

    @Test
    public void nullSafe_whenSerializingNull_writesJsonNull() throws IOException {
        // Arrange
        TypeAdapter<String> nullSafeAdapter = FAILING_ADAPTER.nullSafe();

        // Act
        String json = nullSafeAdapter.toJson(null);

        // Assert
        assertThat(json).isEqualTo("null");
        // The test implicitly verifies that FAILING_ADAPTER.write() was not called.
    }

    @Test
    public void nullSafe_whenDeserializingNull_returnsNull() throws IOException {
        // Arrange
        TypeAdapter<String> nullSafeAdapter = FAILING_ADAPTER.nullSafe();

        // Act
        String value = nullSafeAdapter.fromJson("null");

        // Assert
        assertThat(value).isNull();
        // The test implicitly verifies that FAILING_ADAPTER.read() was not called.
    }
}