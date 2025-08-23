package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Contains tests for the {@link MergedStream} class, focusing on its
 * adherence to the {@link java.io.InputStream} contract.
 */
public class MergedStreamTest {

    /**
     * Verifies that calling {@code reset()} on a {@link MergedStream} instance
     * without a preceding call to {@code mark()} throws an {@link IOException}.
     * This behavior is mandated by the {@link java.io.InputStream#reset()} contract.
     */
    @Test(expected = IOException.class)
    public void resetWithoutMarkShouldThrowIOException() throws IOException {
        // Arrange: Create a MergedStream. The specific contents of the buffer and
        // underlying stream are not relevant for this test case, as we are only
        // testing the reset() behavior.
        byte[] buffer = new byte[8];
        InputStream underlyingStream = new ByteArrayInputStream(new byte[0]);
        
        // The MergedStream is created with an empty prepended buffer (start == end).
        MergedStream mergedStream = new MergedStream(null, underlyingStream, buffer, 1, 1);

        // Act: Attempt to reset the stream without having marked it first.
        // Assert: An IOException is expected, as declared in the @Test annotation.
        mergedStream.reset();
    }
}