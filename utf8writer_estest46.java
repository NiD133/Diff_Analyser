package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/*
 * Note: The original test class name "UTF8Writer_ESTestTest46" and its scaffolding
 * are preserved as they might be part of a larger, auto-generated test suite.
 * A more conventional name would be "UTF8WriterTest".
 */
public class UTF8Writer_ESTestTest46 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Verifies that writing a single-byte ASCII character (value < 128)
     * correctly writes one byte to the underlying output stream.
     */
    @Test
    public void write_whenWritingSingleAsciiChar_thenWritesSingleByte() throws IOException {
        // Arrange: Set up the writer and its dependencies.
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

        int singleByteChar = 2; // A non-printable ASCII control character (STX)

        // Act: Write the character and flush the stream to ensure it's processed.
        utf8Writer.write(singleByteChar);
        utf8Writer.flush();

        // Assert: Verify the output is correct.
        final int expectedByteCount = 1;
        final String expectedStringOutput = "\u0002";

        assertEquals("The output stream should contain exactly one byte.",
                expectedByteCount, outputStream.size());
        assertEquals("The string representation of the output should match the character written.",
                expectedStringOutput, outputStream.toString());
    }
}