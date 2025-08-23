package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link JsonTreeReader} class, focusing on its behavior
 * when encountering unexpected token types.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling nextBoolean() when the current token is the beginning of an object
     * throws an IllegalStateException.
     */
    @Test
    public void nextBoolean_whenTokenIsBeginObject_throwsIllegalStateException() {
        // Arrange: Create a JsonTreeReader for an empty JSON object.
        // The reader's initial state will be at the BEGIN_OBJECT token.
        JsonObject emptyJsonObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(emptyJsonObject);

        // Act & Assert: Attempt to read a boolean value and verify the exception.
        IllegalStateException exception = assertThrows(
            "Expected an IllegalStateException when trying to read a boolean from an object token.",
            IllegalStateException.class,
            () -> reader.nextBoolean()
        );

        // Assert that the exception message is descriptive and correct.
        String expectedMessage = "Expected BOOLEAN but was BEGIN_OBJECT at path $";
        assertEquals(expectedMessage, exception.getMessage());
    }
}