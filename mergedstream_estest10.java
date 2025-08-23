package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * This test suite contains tests for the {@link MergedStream} class.
 * This particular test was improved for clarity from an auto-generated test case.
 */
public class MergedStreamTest {

    /**
     * Tests that calling skip() with a negative value causes a NullPointerException
     * when the MergedStream is in a specific state.
     * <p>
     * This behavior is likely a bug, as skip() should ideally return 0 for negative
     * values. The exception occurs because the method proceeds to free an internal
     * buffer. The test sets up a state where this buffer was not allocated via the
     * IOContext, causing a NullPointerException within the IOContext when it
     * attempts to release a buffer it doesn't "own".
     */
    @Test
    public void skipWithNegativeValueShouldThrowNPEWhenReleasingUnmanagedBuffer() {
        // Arrange

        // 1. Set up an IOContext that does not manage the buffer resource. This is key
        // to the failure, as its internal reference to the read buffer will be null.
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                ContentReference.REDACTED_CONTENT,
                false // `isResourceManaged` is false
        );

        // 2. Create a MergedStream with an invalid buffer state where the start offset
        // is greater than the end offset. This setup is necessary to bypass the
        // initial conditional check in the skip() method, forcing it to execute
        // the buffer-freeing logic.
        byte[] preReadBuffer = new byte[0];
        InputStream underlyingStream = new ByteArrayInputStream(preReadBuffer);
        int invalidStartOffset = 1000;
        int invalidEndOffset = 200; // Note: end < start
        MergedStream mergedStream = new MergedStream(ioContext, underlyingStream, preReadBuffer, invalidStartOffset, invalidEndOffset);

        long negativeBytesToSkip = -240L;

        // Act & Assert
        try {
            mergedStream.skip(negativeBytesToSkip);
            fail("Expected a NullPointerException to be thrown due to incorrect buffer handling.");
        } catch (NullPointerException e) {
            // The NPE is expected. It originates from IOContext when it tries to verify
            // the release of a buffer it never allocated, leading to a null access.
            // We can confirm the source of the exception to ensure the test is failing for the right reason.
            StackTraceElement topOfStack = e.getStackTrace()[0];
            assertEquals("Exception should originate from the IOContext class",
                    IOContext.class.getName(), topOfStack.getClassName());
        } catch (IOException e) {
            fail("Caught an unexpected IOException, but a NullPointerException was expected. Details: " + e.getMessage());
        }
    }
}