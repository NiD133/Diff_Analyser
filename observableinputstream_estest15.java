package org.apache.commons.io.input;

import org.junit.Test;

import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Tests for {@link ObservableInputStream}.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that calling read(byte[], int, int) with parameters that are out of bounds
     * for the buffer propagates the IndexOutOfBoundsException from the underlying stream.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithOutOfBoundsParametersPropagatesException() throws Exception {
        // Arrange: Create an ObservableInputStream wrapping a PipedInputStream.
        // The underlying PipedInputStream is expected to throw the exception, which
        // the ObservableInputStream should propagate.
        final PipedOutputStream pipedOutputStream = new PipedOutputStream();
        final InputStream underlyingStream = new PipedInputStream(pipedOutputStream);
        final ObservableInputStream observableInputStream = new ObservableInputStream(underlyingStream);

        final byte[] emptyBuffer = new byte[0];
        final int invalidOffset = 1; // Any offset > -1 is invalid for an empty buffer.
        final int invalidLength = 1; // Any length > 0 is invalid for an empty buffer.

        // Act: Attempt to read into the buffer with out-of-bounds parameters.
        // This call is expected to throw an IndexOutOfBoundsException.
        observableInputStream.read(emptyBuffer, invalidOffset, invalidLength);

        // Assert: The @Test(expected=...) annotation handles the assertion that an
        // IndexOutOfBoundsException is thrown.
    }
}