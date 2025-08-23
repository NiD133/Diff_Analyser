package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void nextName_whenInEmptyArray_throwsIllegalStateException() throws IOException {
        // Arrange
        JsonArray emptyArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyArray);
        reader.beginArray(); // Position the reader inside the empty array.

        // Act & Assert
        // Calling nextName() is invalid because the next token is END_ARRAY, not NAME.
        IllegalStateException exception = assertThrows(
            "Expected an IllegalStateException when calling nextName() inside an empty array",
            IllegalStateException.class,
            () -> reader.nextName()
        );

        // Verify the exception message to ensure the error is informative.
        assertEquals("Expected NAME but was END_ARRAY at path $[0]", exception.getMessage());
    }
}