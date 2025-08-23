package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link UTF8Writer} class.
 * The original test class name 'UTF8Writer_ESTestTest28' suggests it was auto-generated.
 */
public class UTF8WriterTest {

    /**
     * Tests that calling write() with a negative length is a no-op.
     * <p>
     * This behavior deviates from the standard {@link java.io.Writer#write(char[], int, int)}
     * contract, which specifies that an {@link IndexOutOfBoundsException} should be thrown for a
     * negative length. The implementation in {@link UTF8Writer} handles this case by simply
     * returning. This test verifies that no exception is thrown and, crucially, that no
     * data is written to the underlying output stream.
     */
    @Test
    public void writeWithNegativeLengthShouldDoNothing() throws IOException {
        // Arrange: Set up the necessary IOContext and a UTF8Writer with an in-memory stream.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.unknown(),
                true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(ioContext, outputStream);

        char[] emptyBuffer = new char[0];
        int arbitraryOffset = 1000;
        int negativeLength = -230;

        // Act: Attempt to write with a negative length.
        // This should be a no-op and not throw an exception.
        writer.write(emptyBuffer, arbitraryOffset, negativeLength);
        writer.flush();

        // Assert: Verify that nothing was written to the output stream.
        assertEquals("The output stream should be empty after a write with negative length",
                0, outputStream.size());
    }
}