package com.google.gson.internal.bind;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void writeValueInsideObjectWithoutName_throwsIllegalStateException() throws IOException {
        // Arrange
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();

        // Act & Assert
        // An IllegalStateException is expected because a value cannot be written
        // to a JSON object without a preceding name.
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            // The specific value type (e.g., null Boolean) is not the focus;
            // any value call would fail here.
            jsonTreeWriter.value((Boolean) null);
        });

        // Verify that the exception has no message, matching the original behavior.
        assertNull(exception.getMessage());
    }
}