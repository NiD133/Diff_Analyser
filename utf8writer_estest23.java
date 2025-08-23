package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the {@link UTF8Writer} class.
 * This class focuses on improving the understandability of an auto-generated test.
 */
public class UTF8WriterTest {

    /**
     * Tests that calling write() with a length that extends beyond the
     * string's bounds throws an IndexOutOfBoundsException.
     *
     * The underlying implementation is expected to attempt an access like
     * `string.charAt(offset + length)`, which will fail.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void writeStringWithLengthExceedingBoundsThrowsException() throws IOException {
        // Arrange: Set up the writer and define test inputs.
        BufferRecycler bufferRecycler = new BufferRecycler();
        ContentReference contentReference = ContentReference.unknown();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                contentReference,
                true); // managedResource = true

        // Use a simple ByteArrayOutputStream as the underlying stream.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        String text = "some text"; // length = 9
        int offset = 2;
        // Use a length that, when added to the offset, clearly exceeds the string's bounds.
        int length = text.length(); // (offset + length) = 2 + 9 = 11, which is > 9.

        // Act: Attempt to write the substring.
        // This call is expected to throw an IndexOutOfBoundsException.
        utf8Writer.write(text, offset, length);

        // Assert: The test passes if the expected exception is thrown.
        // This is handled by the @Test(expected = ...) annotation.
    }
}