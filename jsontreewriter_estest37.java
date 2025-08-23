package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that attempting to write a value inside a JSON object
     * without first specifying a name results in an IllegalStateException.
     * According to the JSON specification, object members must be key-value pairs.
     */
    @Test(expected = IllegalStateException.class)
    public void writingValueInsideObjectWithoutNameShouldThrowException() throws IOException {
        // Arrange: Create a writer and start an object context.
        JsonWriter jsonWriter = new JsonTreeWriter();
        jsonWriter.beginObject();

        // Act: Attempt to write a value directly, without a preceding call to name().
        // This action is invalid and should trigger the exception.
        jsonWriter.value("some value");

        // Assert: The test passes if an IllegalStateException is thrown,
        // which is handled by the @Test(expected = ...) annotation.
    }
}