package com.google.gson;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Test for {@link JsonParser}.
 */
public class JsonParserTest {

    @Test
    public void parseReader_withMalformedJsonStartingWithClosingBrace_throwsJsonSyntaxException() {
        // Arrange
        String malformedJson = "}Yz";
        Reader reader = new StringReader(malformedJson);

        // Act & Assert
        JsonSyntaxException thrown = assertThrows(
            JsonSyntaxException.class,
            () -> JsonParser.parseReader(reader)
        );

        // Verify the exception message for more precise feedback
        assertTrue(
            "The exception message should indicate the location of the syntax error.",
            thrown.getMessage().startsWith("Expected value at line 1 column 1")
        );
    }
}