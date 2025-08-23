package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonWriter;
import org.junit.Test;

/**
 * Tests for the fluent API of {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void beginArray_returnsSameWriterInstanceForChaining() throws Exception {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act
        JsonWriter returnedWriter = writer.beginArray();

        // Assert
        // The method should return the same instance to allow for method chaining.
        assertThat(returnedWriter).isSameInstanceAs(writer);
    }
}