package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.io.IOContext;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This test class contains the refactored test case for the MergedStream class.
 * The original test was automatically generated and has been improved for clarity.
 */
public class MergedStream_ESTestTest17 extends MergedStream_ESTest_scaffolding {

    /**
     * Tests that the MergedStream constructor correctly handles buffer boundaries.
     * Specifically, it verifies that calling read() throws an ArrayIndexOutOfBoundsException
     * if the stream is initialized with a starting pointer that is outside the
     * bounds of the provided buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void readShouldThrowExceptionWhenStartPointerIsOutOfBounds() throws IOException {
        // Arrange: Create a MergedStream with a start pointer far beyond the buffer's length.
        byte[] buffer = new byte[1];
        int outOfBoundsStartIndex = 98;
        
        // The underlying stream is not relevant for this test, as the failure occurs
        // before any attempt is made to read from it. A simple empty stream suffices.
        InputStream underlyingStream = new ByteArrayInputStream(new byte[0]);

        MergedStream mergedStream = new MergedStream(
                null, // IOContext is not used in the read() path being tested.
                underlyingStream,
                buffer,
                outOfBoundsStartIndex,
                outOfBoundsStartIndex // The end pointer is also out of bounds.
        );

        // Act & Assert: Attempting to read from the stream should immediately fail
        // because the internal pointer (_ptr) is invalid. The @Test(expected=...)
        // annotation handles the assertion.
        mergedStream.read();
    }
}