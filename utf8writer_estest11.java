package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;

import java.io.IOException;
import java.io.OutputStream;

// Note: The original test class name and scaffolding are preserved as per the prompt's context.
// In a real-world scenario, these would likely be refactored for better clarity.
public class UTF8Writer_ESTestTest11 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Verifies that calling {@code write(char[], int, int)} with an offset that is
     * outside the bounds of the source array throws an {@code IndexOutOfBoundsException}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void writeWithOffsetOutOfBoundsShouldThrowException() throws IOException {
        // Arrange: Set up the writer and define an out-of-bounds write operation.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.unknown(),
                true);

        // A mock output stream is used since the test focuses on input validation,
        // not the actual output.
        OutputStream mockOutputStream = new MockFileOutputStream("test.tmp");
        UTF8Writer writer = new UTF8Writer(ioContext, mockOutputStream);

        char[] buffer = new char[10];
        // Define an offset that is clearly beyond the buffer's capacity.
        int invalidOffset = buffer.length + 1;
        int length = 1;

        // Act: Attempt to write from the buffer with the invalid offset.
        // This call is expected to fail with an IndexOutOfBoundsException.
        writer.write(buffer, invalidOffset, length);

        // Assert: The test succeeds if the expected exception is thrown,
        // which is handled by the @Test(expected = ...) annotation.
    }
}