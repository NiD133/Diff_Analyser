package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;

import static org.junit.Assert.assertArrayEquals;

// The original test class name is kept for context, but in a real project,
// it would be renamed to something like UTF8WriterTest.
public class UTF8Writer_ESTestTest33 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Tests that the append(CharSequence) method correctly encodes a multi-byte
     * character into its UTF-8 byte representation.
     */
    @Test(timeout = 4000)
    public void appendCharSequenceWithMultiByteCharacterShouldEncodeCorrectly() throws IOException {
        // Arrange
        // 1. Create a stream to capture the writer's output.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 2. Set up the necessary IOContext for the UTF8Writer.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext context = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                // Using 'unknown()' is more idiomatic for test setup than 'REDACTED_CONTENT'.
                ContentReference.unknown(),
                true);
        UTF8Writer writer = new UTF8Writer(context, outputStream);

        // 3. Define the input character and its expected UTF-8 byte sequence.
        // The character 'ö' (U+00F6) is encoded as two bytes in UTF-8: 0xC3 and 0xB6.
        char multiByteChar = '\u00F6';
        CharSequence inputSequence = CharBuffer.wrap(new char[]{multiByteChar});
        byte[] expectedUtf8Bytes = new byte[]{(byte) 0xC3, (byte) 0xB6};

        // Act
        writer.append(inputSequence);
        // Close the writer to ensure its internal buffer is flushed to the underlying stream.
        writer.close();

        // Assert
        // Verify that the bytes written to the stream match the expected UTF-8 encoding.
        assertArrayEquals(expectedUtf8Bytes, outputStream.toByteArray());
    }
}