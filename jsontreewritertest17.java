package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for the fluent API of {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that calling {@code value(Boolean)} returns the same writer instance,
     * which is essential for method chaining (fluent interface).
     */
    @Test
    public void value_withBoolean_returnsSameWriterInstanceForChaining() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        Boolean value = true;

        // Act
        JsonWriter returnedWriter = writer.value(value);

        // Assert
        assertThat(returnedWriter).isSameInstanceAs(writer);
    }
}