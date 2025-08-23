package com.google.common.io;

import java.io.Reader;
import java.io.Writer;
import org.junit.Test;

/**
 * Tests for {@link CharStreams#copyReaderToWriter(Reader, Writer)}.
 */
public class CharStreamsCopyTest {

    /**
     * Verifies that copyReaderToWriter throws a NullPointerException when the source Reader is null,
     * as mandated by the Preconditions check.
     */
    @Test(expected = NullPointerException.class)
    public void copyReaderToWriter_whenReaderIsNull_throwsNullPointerException() {
        // Arrange: Create a valid destination writer, but use a null source reader.
        Writer destinationWriter = CharStreams.nullWriter();
        Reader nullReader = null;

        // Act & Assert: Attempting to copy from a null reader should throw a NullPointerException.
        CharStreams.copyReaderToWriter(nullReader, destinationWriter);
    }
}