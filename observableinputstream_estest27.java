package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Unit tests for the {@link ObservableInputStream} class.
 */
public class ObservableInputStreamTest {

    /**
     * Verifies that attempting to consume an ObservableInputStream that was
     * constructed with a null underlying stream results in a NullPointerException.
     * This is the expected behavior as the operation is delegated to the null stream.
     */
    @Test(expected = NullPointerException.class)
    public void consumeWithNullInputStreamThrowsNullPointerException() throws IOException {
        // Arrange: Create an ObservableInputStream with a null delegate stream.
        // The cast to InputStream is necessary to resolve constructor ambiguity.
        final ObservableInputStream stream = new ObservableInputStream((InputStream) null);

        // Act: Attempt to consume the stream. This action is expected to throw the exception.
        stream.consume();
    }
}