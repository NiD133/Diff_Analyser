package org.apache.commons.io.input;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link ObservableInputStream} focusing on its behavior as a proxy.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that an exception thrown by the underlying (wrapped) stream's read() method
     * is correctly propagated by the ObservableInputStream.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void readShouldPropagateExceptionFromUnderlyingStream() throws IOException {
        // Arrange: Create an underlying stream that is guaranteed to fail on read.
        // A ByteArrayInputStream with an invalid negative offset is used to reliably
        // trigger an ArrayIndexOutOfBoundsException.
        byte[] data = {1};
        InputStream faultyUnderlyingStream = new ByteArrayInputStream(data, -1, data.length);
        ObservableInputStream observableStream = new ObservableInputStream(faultyUnderlyingStream);

        // Act: Attempt to read from the stream. This should trigger the exception in the
        // underlying stream, which we expect to be propagated.
        observableStream.read();

        // Assert: The test succeeds if an ArrayIndexOutOfBoundsException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}