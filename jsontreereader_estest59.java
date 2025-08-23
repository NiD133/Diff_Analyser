package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the JsonTreeReader.
 * This specific test was improved for clarity and maintainability.
 */
public class JsonTreeReaderImprovedTest {

    /**
     * Verifies that calling nextNull() when the reader is positioned at a STRING token
     * correctly throws an IllegalStateException.
     */
    @Test
    public void nextNull_whenNextTokenIsString_throwsIllegalStateException() throws IOException {
        // Arrange: Create a JsonTreeReader for an object {"property": "value"}
        // and advance it to be positioned at the string value.
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("property", "value");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        // The skipValue() method, when called after beginObject(), consumes the property name ("property"),
        // leaving the reader positioned at the corresponding value ("value").
        reader.skipValue();

        // Act & Assert: Expect an IllegalStateException when trying to read a NULL
        // because the current token is a STRING.
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            reader::nextNull
        );

        // Assert that the exception message clearly indicates the token type mismatch.
        // The original path was "$.<skipped>", but we focus on the more critical part of the message.
        String expectedMessagePrefix = "Expected NULL but was STRING";
        String actualMessage = exception.getMessage();
        
        assertTrue(
            "Exception message should indicate the token mismatch. Actual message: \"" + actualMessage + "\"",
            actualMessage.startsWith(expectedMessagePrefix)
        );
    }
}