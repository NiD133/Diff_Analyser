package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

/**
 * Contains tests for the {@link UTF8Writer} class.
 * This refactored test corresponds to the original UTF8Writer_ESTestTest38.
 */
public class UTF8WriterRefactoredTest {

    /**
     * Tests that writing a single character that requires three bytes in UTF-8
     * produces the correct byte sequence. The character U+0800 (decimal 2048)
     * is the first code point that requires a three-byte encoding.
     */
    @Test
    public void shouldCorrectlyEncodeThreeByteCharacter() throws IOException {
        // Arrange
        // The character U+0800 (decimal 2048) requires a 3-byte representation in UTF-8.
        final int threeByteChar = 2048;
        // The expected UTF-8 byte sequence for U+0800 is [0xE8, 0x80, 0x80].
        final byte[] expectedBytes = new byte[]{(byte) 0xE8, (byte) 0x80, (byte) 0x80};

        IOContext ioContext = createIOContext();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        // Act
        utf8Writer.write(threeByteChar);
        utf8Writer.close(); // Flushes and closes the writer and underlying stream.

        // Assert
        byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals("The UTF-8 encoding for character 2048 should be 3 bytes long",
                expectedBytes, actualBytes);
    }

    /**
     * Helper method to create a default IOContext for tests.
     */
    private IOContext createIOContext() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        return new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                null, // ContentReference is not relevant for this test
                false); // isResourceManaged
    }
}