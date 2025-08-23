package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;

/**
 * Contains an improved test case for the {@link ObservableInputStream} class.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that calling close() on an ObservableInputStream that wraps a null
     * InputStream throws a NullPointerException. This is the expected behavior,
     * as the close() operation is delegated to the wrapped stream.
     */
    @Test(expected = NullPointerException.class)
    public void closeWithNullWrappedStreamThrowsNullPointerException() throws IOException {
        // Arrange: Create an ObservableInputStream with a null underlying stream.
        final ObservableInputStream observableInputStream = new ObservableInputStream(null);

        // Act & Assert: Calling close() should attempt to close the null stream,
        // resulting in a NullPointerException. The assertion is handled by the
        // @Test(expected=...) annotation.
        observableInputStream.close();
    }
}