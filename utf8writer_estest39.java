package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

/**
 * Contains tests for the {@link UTF8Writer} class.
 */
public class UTF8WriterTest {

    /**
     * Verifies that writing the maximum valid Unicode code point (U+10FFFF)
     * correctly encodes it into its 4-byte UTF-8 representation.
     */
    @Test
    public void write_shouldEncodeMaxUnicodeCodePointCorrectly() throws IOException {
        // Arrange
        // A simpler IOContext constructor can be used for this test, reducing boilerplate.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, ContentReference.rawReference(null), true);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final int MAX_UNICODE_CODE_POINT = 0x10FFFF; // The highest valid Unicode code point (1,114,111)
        // The expected 4-byte UTF-8 representation of U+10FFFF is 0xF4 0x8F 0xBF 0xBF.
        final byte[] expectedBytes = new byte[]{(byte) 0xF4, (byte) 0x8F, (byte) 0xBF, (byte) 0xBF};

        // Act
        // Use try-with-resources to ensure the writer is closed automatically.
        try (UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream)) {
            utf8Writer.write(MAX_UNICODE_CODE_POINT);
        } // close() is called here, which also flushes the buffer.

        // Assert
        byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals("The UTF-8 encoding for the max code point is incorrect.", expectedBytes, actualBytes);
    }
}