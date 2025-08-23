package com.google.common.io;

import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    @Test
    public void skipFully_whenReaderContainsMalformedInput_throwsMalformedInputException() {
        // Arrange: Create a Reader that will encounter a malformed byte sequence when decoding.
        // For UTF-8 (a common default charset), the byte 0xFF is invalid unless it's part of a
        // multi-byte sequence. Here, it stands alone, making it malformed.
        byte[] malformedBytes = {0x41, (byte) 0xFF, 0x42}; // Represents 'A', an invalid byte, then 'B'
        InputStream inputStream = new ByteArrayInputStream(malformedBytes);
        Reader reader = new InputStreamReader(inputStream, Charset.defaultCharset());

        // Act & Assert: Attempting to read or skip past the malformed byte should throw an exception.
        // We use assertThrows to clearly state that we expect MalformedInputException.
        assertThrows(
                MalformedInputException.class,
                () -> CharStreams.skipFully(reader, 10)
        );
    }
}