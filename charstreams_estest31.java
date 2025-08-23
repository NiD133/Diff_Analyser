package com.google.common.io;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for {@link CharStreams}.
 */
// The original class name and hierarchy are kept for context.
public class CharStreams_ESTestTest31 extends CharStreams_ESTest_scaffolding {

    /**
     * Verifies that skipFully() throws an EOFException when attempting to skip
     * more characters than are available in the reader.
     */
    @Test(expected = EOFException.class)
    public void skipFully_whenSkippingPastEndOfStream_throwsEOFException() throws IOException {
        // Arrange: Create a reader with a known, small number of characters.
        String content = "123456789";
        Reader reader = new StringReader(content);
        long charsToSkip = content.length() + 1;

        // Act & Assert: Attempting to skip more characters than exist in the stream
        // should throw an EOFException, as specified by the method's contract.
        CharStreams.skipFully(reader, charsToSkip);
    }
}