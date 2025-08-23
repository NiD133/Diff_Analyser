package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

// Note: The original test class name and scaffolding are preserved for context.
public class UTF8Writer_ESTestTest36 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Verifies that writing a single ASCII character correctly writes its
     * corresponding UTF-8 byte representation to the underlying output stream.
     */
    @Test(timeout = 4000)
    public void write_whenWritingSingleAsciiCharacter_writesCorrectByteToStream() throws IOException {
        // Arrange: Set up the necessary IOContext and a UTF8Writer connected to a byte stream.
        BufferRecycler bufferRecycler = new BufferRecycler();
        // IOContext setup is boilerplate but necessary for the UTF8Writer constructor.
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.unknown(),
                true); // `true` indicates buffer recycling is managed by IOContext

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        // Act: Write a single character and flush the writer's internal buffer to the stream.
        utf8Writer.write("@");
        utf8Writer.flush();

        // Assert: The byte stream should contain the single UTF-8 byte for '@'.
        // The ASCII and UTF-8 representation for '@' is the byte 64 (0x40).
        byte[] expectedBytes = new byte[]{ 64 };
        assertArrayEquals(expectedBytes, outputStream.toByteArray());

        // Clean up resources.
        utf8Writer.close();
    }
}