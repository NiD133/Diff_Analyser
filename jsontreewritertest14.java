package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void beginObject_returnsSameWriterInstanceForChaining() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act
        JsonWriter returnedWriter = writer.beginObject();

        // Assert
        // The method should return the same instance to allow for a fluent API.
        assertThat(returnedWriter).isSameInstanceAs(writer);
    }
}