package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.Reader;
import org.junit.Test;

/**
 * Contains tests for the {@link JsonParser} class, focusing on handling invalid inputs.
 */
public class JsonParserTest {

    /**
     * Verifies that calling {@link JsonParser#parseReader(Reader)} with a null argument
     * correctly throws a {@link NullPointerException}.
     */
    @Test
    public void parseReader_whenReaderIsNull_throwsNullPointerException() {
        // Arrange: The input to be tested is a null Reader.
        Reader nullReader = null;

        // Act & Assert:
        // We use assertThrows to declare that we expect a NullPointerException
        // when JsonParser.parseReader is called with the null input.
        // This is the modern, recommended way to test for exceptions.
        NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> JsonParser.parseReader(nullReader)
        );

        // For a more robust test, we can also verify the exception message.
        // This ensures the exception is thrown for the reason we expect (a null check on the input).
        assertEquals("in == null", exception.getMessage());
    }
}