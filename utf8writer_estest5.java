package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.util.BufferRecycler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.CharBuffer;

/**
 * Contains an improved test case for the UTF8Writer class, focusing on clarity,
 * correctness, and maintainability.
 */
public class UTF8Writer_ESTestTest5 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Tests that appending a CharSequence to the UTF8Writer correctly writes the
     * corresponding UTF-8 byte representation to the underlying output stream.
     *
     * This test replaces a generated test that had a misleading and unrelated assertion.
     * It now properly verifies the behavior of the append() method.
     */
    @Test(timeout = 4000)
    public void append_whenGivenCharSequence_writesEquivalentUTF8Bytes() throws IOException {
        // Arrange: Set up the writer, the input data, and the expected output.
        // A ByteArrayOutputStream is used to capture the written bytes for verification.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // IOContext is required by the UTF8Writer constructor.
        IOContext ioContext = new IOContext(new BufferRecycler(), ContentReference.unknown(), false);
        Writer writer = new UTF8Writer(ioContext, outputStream);

        // The input CharSequence consists of two null characters ('\u0000').
        char[] inputChars = new char[]{'\u0000', '\u0000'};
        CharSequence inputSequence = CharBuffer.wrap(inputChars);

        // In UTF-8, a null character is represented by a single zero byte.
        byte[] expectedBytes = new byte[]{0, 0};

        // Act: Append the character sequence to the writer and close it to flush the buffer.
        writer.append(inputSequence);
        writer.close();

        // Assert: Verify that the bytes written to the stream match the expected UTF-8 sequence.
        byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals("The output bytes should match the expected UTF-8 representation.", expectedBytes, actualBytes);
    }
}