package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Test;

/**
 * Tests for {@link CharStreams#copy(Readable, Appendable)}.
 */
public class CharStreamsCopyTest extends IoTestCase {

    @Test
    public void testCopy_fromReaderToWriter_copiesAsciiCharacters() throws IOException {
        // Arrange
        StringReader reader = new StringReader(ASCII);
        StringWriter writer = new StringWriter();

        // Act
        long copiedCharCount = CharStreams.copy(reader, writer);

        // Assert
        assertEquals("The copied content should match the source ASCII string.",
                ASCII, writer.toString());
        assertEquals("The returned count should match the source string length.",
                ASCII.length(), copiedCharCount);
    }

    @Test
    public void testCopy_fromReaderToWriter_copiesI18nCharacters() throws IOException {
        // Arrange
        StringReader reader = new StringReader(I18N);
        StringWriter writer = new StringWriter();

        // Act
        long copiedCharCount = CharStreams.copy(reader, writer);

        // Assert
        assertEquals("The copied content should match the source I18N string.",
                I18N, writer.toString());
        assertEquals("The returned count should match the source string length.",
                I18N.length(), copiedCharCount);
    }
}