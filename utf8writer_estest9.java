package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UTF8Writer_ESTestTest9 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Tests that writing content smaller than the internal buffer size does not
     * trigger an immediate write to the underlying output stream. This verifies
     * the expected buffering behavior of the UTF8Writer.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(timeout = 4000)
    public void testWriteBelowBufferSizeShouldNotFlushAutomatically() throws IOException {
        // Arrange: Set up the IOContext and a UTF8Writer with a ByteArrayOutputStream
        // to capture any data that gets written.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.unknown(),
                false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        // Prepare a character array. The character '\u0080' requires two bytes in UTF-8,
        // ensuring we test with multi-byte characters. The total encoded size should
        // easily fit within the writer's default internal buffer.
        char[] charsToWrite = new char[8];
        charsToWrite[0] = '\u0080';

        // Act: Write the character array to the writer.
        utf8Writer.write(charsToWrite);

        // Assert: The underlying output stream should still be empty. This is because
        // the UTF8Writer buffers the output and has not been flushed or filled yet.
        assertEquals("The output stream should be empty as the writer's buffer has not been flushed.",
                0, outputStream.size());
    }
}