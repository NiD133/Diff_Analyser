package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains tests for {@link JsonTreeReader}.
 * This particular test was refactored for clarity.
 */
public class JsonTreeReader_ESTestTest75 /* extends JsonTreeReader_ESTest_scaffolding */ {

    /**
     * Verifies that calling nextLong() when the reader is positioned at the beginning
     * of a JSON array throws an IllegalStateException. The reader expects a NUMBER
     * token but finds a BEGIN_ARRAY token instead.
     */
    @Test
    public void nextLong_whenTokenIsBeginArray_throwsIllegalStateException() {
        // Arrange: Create a JsonTreeReader for an empty JSON array.
        // The reader's initial state points to the BEGIN_ARRAY token.
        JsonArray emptyArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyArray);

        // Act & Assert: Attempting to read a long should fail with a clear error.
        try {
            reader.nextLong();
            fail("Expected an IllegalStateException to be thrown, but it wasn't.");
        } catch (IllegalStateException e) {
            // Verify that the exception message is correct and helpful.
            assertEquals("Expected NUMBER but was BEGIN_ARRAY at path $", e.getMessage());
        }
    }
}