package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Test suite for {@link JsonParser}.
 */
public class JsonParserTest {

    /**
     * Tests that parsing a malformed JSON string from a Reader throws a JsonSyntaxException.
     * The input string is intentionally invalid to trigger a parsing error at the very first character.
     */
    @Test
    public void parseReader_withMalformedJson_shouldThrowJsonSyntaxException() {
        // Arrange: Create a reader with an invalid JSON string.
        // The string starts with '=', which is not a valid start for any JSON value.
        final String malformedJson = "=PC x;DM1DK}c/;";
        Reader reader = new StringReader(malformedJson);

        // Act & Assert
        try {
            JsonParser.parseReader(reader);
            fail("A JsonSyntaxException should have been thrown for the malformed JSON input.");
        } catch (JsonSyntaxException e) {
            // Verify that the exception is of the expected type and has the correct cause.
            assertNotNull("The exception cause should not be null.", e.getCause());
            assertTrue("The exception should be caused by a MalformedJsonException.",
                    e.getCause() instanceof MalformedJsonException);

            // Verify the error message to ensure the correct parsing error was identified.
            // The message indicates the error occurred at the very beginning of the input.
            String expectedMessage = "Expected value at line 1 column 1 path $";
            assertEquals(expectedMessage, e.getCause().getMessage());
        }
    }
}