package com.google.gson.internal.bind;

import static org.junit.Assert.assertSame;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for the {@link JsonTreeWriter} class.
 * This improved test focuses on clarity and adherence to best practices.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that the value() method returns the same writer instance,
     * which is a key feature for supporting a fluent, chainable API.
     */
    @Test
    public void value_shouldReturnSameInstanceForChaining() throws IOException {
        // Arrange: Create a new JsonTreeWriter instance.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Call the value() method and capture the returned instance.
        JsonWriter returnedWriter = writer.value(-1.0F);

        // Assert: The returned instance should be the same as the original.
        // This confirms the method supports the fluent API pattern (e.g., writer.value(1).value("a")).
        assertSame("The writer instance returned by value() should be the same as the original.",
                writer, returnedWriter);
    }
}