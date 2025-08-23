package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link ToNumberPolicy}.
 */
public class ToNumberPolicyTest {

    /**
     * Verifies that the BIG_DECIMAL policy throws a JsonParseException when it encounters
     * an unquoted string that is not a valid number, even in lenient mode.
     */
    @Test
    public void bigDecimalPolicy_whenReadingInvalidStringInLenientMode_throwsJsonParseException() {
        // Arrange
        ToNumberStrategy bigDecimalPolicy = ToNumberPolicy.BIG_DECIMAL;
        String invalidNumberJson = "not-a-number"; // A clear, non-numeric string
        StringReader stringReader = new StringReader(invalidNumberJson);
        JsonReader jsonReader = new JsonReader(stringReader);

        // Lenient mode is required to allow the parser to read an unquoted string value.
        jsonReader.setLenient(true);

        // Act & Assert
        JsonParseException exception = assertThrows(
            JsonParseException.class,
            () -> bigDecimalPolicy.readNumber(jsonReader)
        );

        // Verify the exception message to ensure it failed for the expected reason.
        assertEquals("Cannot parse not-a-number; at path $", exception.getMessage());
    }
}