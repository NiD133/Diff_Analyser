package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link CharStreams#copyReaderToBuilder(java.io.Reader, StringBuilder)}.
 */
public class CharStreamsTest {

    @Test
    public void copyReaderToBuilder_copiesAllCharactersAndReturnsCorrectCount() throws IOException {
        // Arrange
        String inputString = "com.google.common.primitives.Shorts$ShortConverter";
        StringReader reader = new StringReader(inputString);
        StringBuilder destinationBuilder = new StringBuilder();

        // Act
        long numberOfCharsCopied = CharStreams.copyReaderToBuilder(reader, destinationBuilder);

        // Assert
        // Verify that the entire string was copied to the StringBuilder.
        assertEquals(inputString, destinationBuilder.toString());

        // Verify that the returned count matches the input string's length.
        assertEquals((long) inputString.length(), numberOfCharsCopied);
    }
}