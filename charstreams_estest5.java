package com.google.common.io;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;

/**
 * Tests for {@link CharStreams#toString(Readable)}.
 */
public class CharStreamsTest {

    @Test(expected = MalformedInputException.class)
    public void toString_whenReadableContainsMalformedUtf8_throwsMalformedInputException() throws IOException {
        // GIVEN a Reader that wraps a byte stream with an incomplete UTF-8 character sequence.
        // The byte 0xC2 (-62) starts a two-byte sequence, but the second byte is missing.
        byte[] malformedUtf8Bytes = {(byte) 0xC2};
        InputStream inputStream = new ByteArrayInputStream(malformedUtf8Bytes);
        Reader readerWithMalformedInput = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        // WHEN toString is called on the reader
        CharStreams.toString(readerWithMalformedInput);

        // THEN a MalformedInputException is thrown.
    }
}