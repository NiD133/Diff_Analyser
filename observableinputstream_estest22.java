package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import java.io.PipedInputStream;

/**
 * Tests for {@link ObservableInputStream} focusing on exception propagation from the underlying stream.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that an {@link IOException} thrown by the underlying stream is correctly
     * propagated by the {@link ObservableInputStream#read()} method.
     */
    @Test(expected = IOException.class, timeout = 4000)
    public void readShouldPropagateIOExceptionFromUnderlyingStream() throws IOException {
        // Arrange: Create an ObservableInputStream with an underlying stream that is
        // guaranteed to throw an IOException on read. A PipedInputStream that is not
        // connected to a PipedOutputStream serves this purpose.
        final PipedInputStream unconnectedPipe = new PipedInputStream();
        final ObservableInputStream observableStream = new ObservableInputStream(unconnectedPipe);

        // Act: Attempt to read from the stream. This should trigger the IOException
        // from the unconnected pipe, which is expected to be propagated.
        observableStream.read();
    }
}