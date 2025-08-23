package com.google.gson.internal.bind;

import static org.junit.Assert.assertSame;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonTreeWriter} class.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that the {@code value(float)} method returns the same writer instance,
     * which is a common pattern for enabling method chaining (fluent API).
     */
    @Test
    public void valueFloat_returnsSameWriterInstanceForChaining() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act
        JsonWriter returnedWriter = writer.value(-42.784203F);

        // Assert
        // The method should return 'this' to allow for fluent method calls.
        assertSame("The writer instance returned should be the same as the original.", writer, returnedWriter);
    }
}