package com.google.gson;

import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Test suite for the {@link JsonParser} class.
 */
public class JsonParserTest {

    /**
     * Verifies that attempting to parse an empty string, which is not a valid
     * JSON document, correctly throws a {@link JsonSyntaxException}.
     */
    @Test(expected = JsonSyntaxException.class)
    public void parseReader_withEmptyInput_shouldThrowJsonSyntaxException() {
        // Arrange: Create a reader for an empty string.
        Reader emptyReader = new StringReader("");

        // Act: Attempt to parse the empty reader.
        // This call is expected to throw the exception.
        JsonParser.parseReader(emptyReader);

        // Assert: The test passes if a JsonSyntaxException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}