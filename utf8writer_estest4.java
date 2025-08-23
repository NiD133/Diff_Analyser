package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Contains tests for the {@link UTF8Writer} class, focusing on its character encoding capabilities.
 */
public class UTF8Writer_ESTestTest4 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Tests that the append(CharSequence) method correctly encodes a character that
     * requires multiple bytes in UTF-8. The character U+0080 (decimal 128)
     * should be encoded into the byte sequence {0xC2, 0x80}.
     */
    @Test(timeout = 4000)
    public void shouldCorrectlyEncodeMultiByteCharacterWhenAppendingCharSequence() throws Throwable {
        // Arrange: Set up the necessary context, output stream, and writer.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.unknown(),
                true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, byteArrayOutputStream);

        // The input character U+0080 requires two bytes in UTF-8.
        char[] inputChars = {'\u0080'};
        CharSequence charSequence = CharBuffer.wrap(inputChars);
        byte[] expectedBytes = {(byte) 0xC2, (byte) 0x80};

        // Act: Append the character sequence and close the writer to ensure flushing.
        utf8Writer.append(charSequence);
        utf8Writer.close();

        // Assert: Verify that the output bytes match the expected UTF-8 encoding.
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }
}