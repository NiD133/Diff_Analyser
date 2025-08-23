package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * This class contains tests for the {@link JsonTreeWriter}.
 * This specific test was refactored for clarity.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that attempting to write a value directly into a JSON object
     * without first specifying a property name throws an {@link IllegalStateException}.
     * A JSON object must consist of name-value pairs.
     */
    @Test
    public void writingValueInObjectWithoutNameShouldThrowIllegalStateException() throws IOException {
        // Arrange: Create a writer and start a JSON object.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();

        // Act & Assert: Attempting to write a value should fail.
        try {
            writer.value(-1.0F);
            fail("Expected an IllegalStateException because a name must be provided before a value in an object.");
        } catch (IllegalStateException expected) {
            // The original test verified that the exception message is null, which is an
            // important detail of the implementation's behavior. We preserve that check.
            assertEquals(null, expected.getMessage());
        }
    }
}