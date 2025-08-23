package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling nextString() when the current token is the beginning of an array
     * results in an IllegalStateException.
     */
    @Test
    public void nextString_whenTokenIsBeginArray_throwsIllegalStateException() {
        // Arrange: Create a reader for an empty JSON array.
        // The first token to be read will be BEGIN_ARRAY.
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());

        // Act & Assert: Verify that calling nextString() throws the expected exception.
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            reader::nextString
        );

        // Assert: Check the exception message for correctness.
        assertEquals("Expected STRING but was BEGIN_ARRAY at path $", exception.getMessage());
    }
}