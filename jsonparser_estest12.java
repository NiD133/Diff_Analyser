package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonParser} class.
 */
public class JsonParserTest {

    /**
     * Verifies that parsing a malformed JSON string throws a JsonSyntaxException.
     */
    @Test
    public void parseString_shouldThrowExceptionForMalformedJson() {
        // Arrange: An unquoted literal string that is not 'true', 'false', or 'null'
        // is considered malformed JSON.
        String malformedJson = "lP ?";

        // Act & Assert
        try {
            JsonParser.parseString(malformedJson);
            fail("Expected JsonSyntaxException was not thrown for malformed input.");
        } catch (JsonSyntaxException e) {
            // Success: The expected exception was caught.
            // For a more robust test, we can verify that the exception message
            // clearly indicates a parsing problem.
            String expectedMessageFragment = "malformed JSON";
            assertTrue(
                "Exception message should indicate a syntax error. Actual: " + e.getMessage(),
                e.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}