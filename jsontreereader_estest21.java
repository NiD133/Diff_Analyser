package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link JsonTreeReader} class.
 */
public class JsonTreeReaderTest {

    @Test
    public void nextLong_onNonNumericString_throwsNumberFormatException() throws IOException {
        // Arrange: Create a JsonTreeReader with a string that cannot be parsed as a long.
        // The specific string is from the original test to ensure behavior is preserved.
        String nonNumericString = "||9{dvk.\"Ana";
        JsonPrimitive nonNumericPrimitive = new JsonPrimitive(nonNumericString);
        JsonTreeReader reader = new JsonTreeReader(nonNumericPrimitive);

        // Act & Assert: Verify that calling nextLong() throws the expected exception.
        try {
            reader.nextLong();
            fail("Expected NumberFormatException was not thrown for a non-numeric string.");
        } catch (NumberFormatException e) {
            // Verify the exception message to confirm it's caused by the correct input.
            // This behavior is delegated to Long.parseLong() via JsonPrimitive.getAsLong().
            String expectedMessage = "For input string: \"" + nonNumericString + "\"";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}