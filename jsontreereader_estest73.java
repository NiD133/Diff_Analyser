package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling nextInt() when the reader is positioned at the beginning of a JSON array
     * throws an IllegalStateException, as a number is expected instead.
     */
    @Test
    public void nextInt_whenTokenIsBeginArray_throwsIllegalStateException() throws IOException {
        // Arrange: Create a JsonTreeReader for an empty JSON array.
        // The reader's initial state will be at the BEGIN_ARRAY token.
        JsonArray emptyArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyArray);

        // Act & Assert: Attempting to read an integer should fail with a specific message.
        try {
            reader.nextInt();
            fail("Expected an IllegalStateException to be thrown, but no exception occurred.");
        } catch (IllegalStateException e) {
            // Verify that the exception message is correct and informative.
            assertEquals("Expected NUMBER but was BEGIN_ARRAY at path $", e.getMessage());
        }
    }
}