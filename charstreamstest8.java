package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for {@link CharStreams#copy(Readable, Appendable)}.
 */
public class CharStreamsCopyTest extends IoTestCase {

    public void testCopy_toStringBuilder_withAsciiCharacters() throws IOException {
        // Arrange
        String sourceString = ASCII;
        Reader sourceReader = new StringReader(sourceString);
        StringBuilder destinationBuilder = new StringBuilder();

        // Act
        long copiedCharCount = CharStreams.copy(sourceReader, destinationBuilder);

        // Assert
        assertEquals(
                "The number of copied characters should match the source string length.",
                sourceString.length(),
                copiedCharCount);
        assertEquals(
                "The destination builder content should match the source string.",
                sourceString,
                destinationBuilder.toString());
    }

    public void testCopy_toStringBuilder_withI18nCharacters() throws IOException {
        // Arrange
        String sourceString = I18N;
        Reader sourceReader = new StringReader(sourceString);
        StringBuilder destinationBuilder = new StringBuilder();

        // Act
        long copiedCharCount = CharStreams.copy(sourceReader, destinationBuilder);

        // Assert
        assertEquals(
                "The number of copied characters should match the source string length.",
                sourceString.length(),
                copiedCharCount);
        assertEquals(
                "The destination builder content should match the source string.",
                sourceString,
                destinationBuilder.toString());
    }
}