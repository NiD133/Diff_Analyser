package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JsonTreeReader}, focusing on its behavior after being closed.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling skipValue() on a closed reader throws an IllegalStateException.
     */
    @Test
    public void skipValueOnClosedReaderThrowsIllegalStateException() throws IOException {
        // Arrange: Create a reader for an empty JSON array and then close it.
        JsonArray emptyJsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyJsonArray);
        reader.close();

        // Act & Assert: Attempting to skip a value should fail.
        try {
            reader.skipValue();
            fail("Expected an IllegalStateException because the reader is closed, but no exception was thrown.");
        } catch (IllegalStateException expected) {
            // Verify that the exception has the expected message.
            assertEquals("JsonReader is closed", expected.getMessage());
        }
    }
}