package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Contains tests for the {@link UTF8Writer} class, focusing on its bounds checking.
 */
// The original test class name is preserved as requested.
// In a real-world scenario, it would be renamed to "UTF8WriterTest".
public class UTF8Writer_ESTestTest42 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Verifies that the {@code write(char[], int, int)} method throws an
     * {@link ArrayIndexOutOfBoundsException} when the specified length
     * would cause an access beyond the end of the source character array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void write_shouldThrowException_whenLengthExceedsArrayBounds() throws Throwable {
        // Arrange: Set up the writer and the input buffer.
        // The IOContext setup is verbose but necessary for the UTF8Writer constructor.
        // We use default configurations as they are not relevant to this test case.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.rawReference("N/A"),
                true);

        // Using a real (but discardable) stream is safer and better practice than passing null.
        OutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(ioContext, outputStream);

        char[] sourceBuffer = new char[10];
        int offset = 1;
        // Choose a length that, when added to the offset, clearly exceeds the buffer's capacity.
        // Accessing sourceBuffer[offset + length - 1] would be out of bounds.
        int length = sourceBuffer.length; // 1 + 10 > 10, so this will fail.

        // Act: Attempt to write from the buffer with out-of-bounds parameters.
        // The exception is expected to be thrown by this call.
        writer.write(sourceBuffer, offset, length);

        // Assert: The test fails if no exception is thrown.
        // The expected exception is declared in the @Test annotation, which is a standard
        // JUnit 4 pattern for verifying exceptions.
    }
}