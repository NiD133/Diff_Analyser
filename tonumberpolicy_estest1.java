package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link ToNumberPolicy#LONG_OR_DOUBLE}.
 */
public class ToNumberPolicyTest {

    /**
     * Verifies that the LONG_OR_DOUBLE policy throws a JsonParseException when it
     * encounters a JSON string that cannot be parsed as a Long or a Double.
     */
    @Test
    public void longOrDoublePolicy_shouldThrowExceptionForNonNumericString() throws IOException {
        // Arrange
        ToNumberStrategy policy = ToNumberPolicy.LONG_OR_DOUBLE;
        // The input is a valid JSON string, but its content is not a valid number.
        String json = "\"not-a-number\"";
        JsonReader jsonReader = new JsonReader(new StringReader(json));

        // Act & Assert
        try {
            policy.readNumber(jsonReader);
            fail("Expected a JsonParseException to be thrown, but no exception was thrown.");
        } catch (JsonParseException expected) {
            // Verify that the exception message clearly indicates the parsing failure.
            assertEquals("Cannot parse not-a-number; at path $", expected.getMessage());
        }
    }
}