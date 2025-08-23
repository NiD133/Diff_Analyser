package com.google.gson.internal.bind;

import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter} focusing on illegal state transitions.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that calling endArray() is not allowed when the writer is expecting a
     * property value within a JSON object.
     */
    @Test
    public void endArray_whenWritingObjectProperty_throwsIllegalStateException() throws IOException {
        // Arrange: Start writing an object and provide a name for a property.
        // The writer is now in a state where it expects a value to be written.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("property");

        // Act & Assert: Attempting to end an array at this point is an invalid state transition
        // and should throw an IllegalStateException.
        try {
            writer.endArray();
            fail("An IllegalStateException should have been thrown.");
        } catch (IllegalStateException expected) {
            // This is the expected outcome, so the test passes.
        }
    }
}