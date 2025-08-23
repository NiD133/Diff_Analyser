package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link JsonTreeReader}.
 */
class JsonTreeReaderTest {

    @Test
    void nextJsonElement_whenAtEndOfEmptyArray_throwsIllegalStateException() {
        // Arrange
        JsonArray emptyArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyArray);
        
        // To position the reader at the end of the array, we must first consume the BEGIN_ARRAY token.
        // According to the JsonReader API, this can be done by calling beginArray().
        try {
            reader.beginArray();
        } catch (Exception e) {
            // This is part of the setup and is not expected to fail.
            // If it does, we wrap it in an unchecked exception to fail the test.
            throw new AssertionError("Test setup failed: could not begin array", e);
        }

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reader.nextJsonElement();
        }, "Expected nextJsonElement() to throw when no elements are left in the array.");

        assertEquals("Unexpected END_ARRAY when reading a JsonElement.", exception.getMessage());
    }
}