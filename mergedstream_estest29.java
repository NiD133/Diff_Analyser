package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link MergedStream} class.
 */
public class MergedStreamTest {

    /**
     * Tests that MergedStream correctly reports mark support based on its underlying stream.
     * If the wrapped InputStream supports mark(), MergedStream should also report that it does.
     */
    @Test
    public void givenUnderlyingStreamSupportsMark_whenMarkSupportedIsCalled_thenReturnsTrue() throws IOException {
        // Arrange
        // 1. Set up an IOContext, which is a required dependency for MergedStream.
        // The specific configuration details are not critical for this test.
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                ContentReference.unknown(),
                false);

        // 2. Create an underlying input stream that supports marking.
        //    BufferedInputStream is a standard example of such a stream.
        InputStream underlyingStream = new BufferedInputStream(new PipedInputStream());

        // 3. Instantiate MergedStream with the mark-supporting underlying stream.
        //    The byte buffer is null for this scenario, so the stream will read
        //    directly from the underlying source.
        MergedStream mergedStream = new MergedStream(ioContext, underlyingStream, null, 0, 0);

        // Act
        boolean isMarkSupported = mergedStream.markSupported();

        // Assert
        // MergedStream should delegate the markSupported() check to the underlying stream.
        // Since BufferedInputStream supports marking, the result must be true.
        assertTrue("MergedStream should report mark support when the underlying stream does.", isMarkSupported);
        
        // Cleanup
        mergedStream.close();
    }
}