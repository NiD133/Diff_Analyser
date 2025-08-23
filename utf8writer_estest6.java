package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the {@link UTF8Writer} class, focusing on its character encoding capabilities.
 */
public class UTF8Writer_ESTestTest6 {

    /**
     * Tests that the {@code write(int)} method correctly encodes a single character
     * that requires a two-byte representation in UTF-8.
     *
     * The character with code point 128 is the first to require two bytes in UTF-8 encoding.
     * This test verifies that the writer produces the correct two-byte sequence: 0xC2, 0x80.
     */
    @Test(timeout = 4000)
    public void write_shouldCorrectlyEncodeCharacterRequiringTwoBytes() throws IOException {
        // Arrange: Set up the necessary context and output stream for the UTF8Writer.
        // These are standard boilerplate objects needed to instantiate IOContext.
        StreamReadConstraints streamReadConstraints = StreamReadConstraints.defaults();
        StreamWriteConstraints streamWriteConstraints = StreamWriteConstraints.defaults();
        ErrorReportConfiguration errorReportConfiguration = ErrorReportConfiguration.defaults();
        BufferRecycler bufferRecycler = new BufferRecycler();
        ContentReference contentReference = ContentReference.unknown();
        IOContext ioContext = new IOContext(streamReadConstraints, streamWriteConstraints, errorReportConfiguration, bufferRecycler, contentReference, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        // The expected UTF-8 byte sequence for the character with code point 128.
        byte[] expectedBytes = new byte[]{(byte) 0xC2, (byte) 0x80};

        // Act: Write the character with code point 128.
        utf8Writer.write(128);
        // Close the writer to ensure all buffered content is flushed to the underlying stream.
        utf8Writer.close();

        // Assert: Verify that the output stream contains the correct UTF-8 byte sequence.
        assertArrayEquals("The character with code point 128 should be encoded as a two-byte sequence.", expectedBytes, outputStream.toByteArray());
    }
}