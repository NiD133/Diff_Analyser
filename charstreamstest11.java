package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.CharBuffer;

/**
 * Tests for {@link CharStreams#copy(Readable, Appendable)}, specifically ensuring it works
 * correctly when the source is a generic {@link Readable} instance. This forces the use of the
 * non-optimized code path that handles any Readable, not just specific subtypes like
 * {@link StringReader}.
 */
public class CharStreamsCopyFromGenericReadableTest extends IoTestCase {

    /**
     * Wraps a Readable in a new anonymous class to prevent the code under test from using
     * type-specific optimizations, forcing it to use the generic {@code Readable} logic.
     */
    private static Readable asGenericReadable(Readable readable) {
        return new Readable() {
            @Override
            public int read(CharBuffer cb) throws IOException {
                return readable.read(cb);
            }
        };
    }

    public void testCopy_fromGenericReadable_copiesAsciiCharacters() throws IOException {
        // Arrange
        StringReader reader = new StringReader(ASCII);
        Readable genericReader = asGenericReadable(reader);
        StringWriter writer = new StringWriter();

        // Act
        long copiedCharCount = CharStreams.copy(genericReader, writer);

        // Assert
        assertEquals(ASCII, writer.toString());
        assertEquals(ASCII.length(), copiedCharCount);
    }

    public void testCopy_fromGenericReadable_copiesI18nCharacters() throws IOException {
        // Arrange
        StringReader reader = new StringReader(I18N);
        Readable genericReader = asGenericReadable(reader);
        StringWriter writer = new StringWriter();

        // Act
        long copiedCharCount = CharStreams.copy(genericReader, writer);

        // Assert
        assertEquals(I18N, writer.toString());
        assertEquals(I18N.length(), copiedCharCount);
    }
}