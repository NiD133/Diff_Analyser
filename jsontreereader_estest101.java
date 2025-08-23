package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link JsonTreeReader}.
 * This class focuses on verifying exception handling for invalid state transitions.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling {@code endObject()} when the reader is positioned
     * at the start of an array correctly throws an {@link IllegalStateException}.
     * This tests the reader's ability to enforce valid JSON structure, preventing
     * the closing of an object when an array context is active.
     */
    @Test
    public void endObject_whenAtStartOfArray_throwsIllegalStateException() throws IOException {
        // Arrange: Create a reader for an empty JSON array.
        // The reader's initial state corresponds to the BEGIN_ARRAY token.
        JsonArray emptyArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyArray);

        // Act & Assert: Attempt to call endObject() and verify the resulting exception.
        try {
            reader.endObject();
            fail("Expected an IllegalStateException to be thrown, but no exception occurred.");
        } catch (IllegalStateException e) {
            // Verify that the exception message clearly indicates the state mismatch and location.
            assertEquals("Expected END_OBJECT but was BEGIN_ARRAY at path $", e.getMessage());
        }
    }
}