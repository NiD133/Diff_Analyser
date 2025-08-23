package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

/**
 * Tests for {@link CharStreams#copy(Readable, Appendable)}.
 */
public class CharStreamsCopyTest {

    @Test
    public void copy_fromReadableToAppendable_copiesAllCharactersAndReturnsCount() throws IOException {
        // Arrange
        String inputString = "This is a test string.";
        StringReader sourceReader = new StringReader(inputString);
        StringBuilder destinationBuilder = new StringBuilder();

        // Act
        long charsCopied = CharStreams.copy(sourceReader, destinationBuilder);

        // Assert
        assertEquals("The destination should contain the full input string.",
                inputString, destinationBuilder.toString());
        assertEquals("The returned count should match the input string's length.",
                (long) inputString.length(), charsCopied);
    }
}