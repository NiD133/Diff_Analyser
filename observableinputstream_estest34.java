package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Contains tests for {@link ObservableInputStream}.
 * This test class focuses on behavior when the underlying stream is null.
 */
public class ObservableInputStream_ESTestTest34 extends ObservableInputStream_ESTest_scaffolding {

    /**
     * Verifies that attempting to read from an ObservableInputStream that was
     * constructed with a null underlying stream results in a NullPointerException.
     * The read operation is delegated to the underlying stream, which, being null,
     * causes the exception.
     */
    @Test(expected = NullPointerException.class)
    public void readFromNullInputStreamShouldThrowNullPointerException() throws IOException {
        // Arrange: Create an ObservableInputStream with a null source stream.
        final ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        final byte[] buffer = new byte[16];

        // Act: Attempt to read from the stream. This is expected to throw the NullPointerException.
        observableInputStream.read(buffer, 0, buffer.length);

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // in the @Test annotation.
    }
}