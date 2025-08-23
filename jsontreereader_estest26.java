package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link JsonTreeReader} class.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling {@link JsonTreeReader#nextInt()} on a JSON primitive
     * containing a non-numeric string correctly throws a {@link NumberFormatException}.
     */
    @Test
    public void nextInt_onNonNumericString_throwsNumberFormatException() {
        // Arrange: Create a reader for a JSON primitive that is a non-numeric string.
        String nonNumericValue = "not a number";
        JsonPrimitive jsonPrimitive = new JsonPrimitive(nonNumericValue);
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        // Act & Assert: Verify that calling nextInt() throws the expected exception.
        try {
            reader.nextInt();
            fail("A NumberFormatException was expected but not thrown.");
        } catch (NumberFormatException expected) {
            // Verify the exception message is informative and contains the problematic input.
            String expectedMessage = "For input string: \"" + nonNumericValue + "\"";
            assertEquals(expectedMessage, expected.getMessage());
        }
    }
}