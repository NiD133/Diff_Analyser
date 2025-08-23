package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertSame;

/**
 * This class contains tests for the {@link JsonTreeWriter}.
 * This specific test was improved for understandability.
 */
public class JsonTreeWriter_ESTestTest22 extends JsonTreeWriter_ESTest_scaffolding {

    /**
     * Verifies that the {@code name()} method returns the same writer instance,
     * which is essential for supporting a fluent API (method chaining).
     */
    @Test
    public void name_shouldReturnSameWriterInstanceForChaining() throws IOException {
        // Arrange
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();

        // Act
        // Call the name() method to set a property name.
        JsonWriter result = jsonTreeWriter.name("test_property");

        // Assert
        // The method should return the same instance to allow calls like: writer.name("a").value(1);
        assertSame("name() should return the same JsonWriter instance.", jsonTreeWriter, result);
    }
}