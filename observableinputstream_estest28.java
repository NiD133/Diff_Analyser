package org.apache.commons.io.input;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link ObservableInputStream} focusing on exception propagation.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that an exception from the underlying stream is propagated
     * when {@link ObservableInputStream#consume()} is called.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void consumeShouldPropagateExceptionFromUnderlyingStream() throws IOException {
        // Arrange: Create an underlying stream that is guaranteed to throw an
        // ArrayIndexOutOfBoundsException upon reading due to an invalid negative offset.
        final byte[] dummyData = new byte[10];
        final InputStream failingStream = new ByteArrayInputStream(dummyData, -1, 1);
        final ObservableInputStream observableStream = new ObservableInputStream(failingStream);

        // Act: Calling consume() will trigger a read on the failing underlying stream.
        // The test asserts that the expected exception is thrown and propagated.
        observableStream.consume();
    }
}