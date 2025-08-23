package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This test class contains the refactored test case.
 * The original scaffolding class is extended to maintain structural consistency.
 */
public class MergedStream_ESTestTest21 extends MergedStream_ESTest_scaffolding {

    /**
     * Verifies that calling close() on a MergedStream is safe
     * even when its internal buffer is null. This prevents potential
     * NullPointerExceptions during resource cleanup.
     */
    @Test
    public void closeShouldSucceedEvenIfBufferIsNull() throws IOException {
        // Arrange
        // 1. Create a minimal, valid IOContext required by the MergedStream constructor.
        BufferRecycler bufferRecycler = new BufferRecycler();
        ContentReference contentReference = ContentReference.redacted();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                contentReference,
                true);

        // 2. Use an empty, non-null underlying stream.
        InputStream emptyUnderlyingStream = new ByteArrayInputStream(new byte[0]);

        // 3. Instantiate MergedStream with a null buffer. The start/end pointers are
        //    irrelevant in this case but must be provided.
        MergedStream mergedStream = new MergedStream(ioContext, emptyUnderlyingStream, null, 0, 0);

        // Act & Assert
        // The test's goal is to ensure that close() executes without throwing an exception.
        // In JUnit 4, the absence of an exception indicates a passing test.
        mergedStream.close();
    }
}