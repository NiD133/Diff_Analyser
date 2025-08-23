package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;

/**
 * Contains tests for the {@link ProxyReader} class.
 */
public class ProxyReaderTest {

    /**
     * Tests that the {@code handleIOException} method throws a
     * {@code NullPointerException} when passed a null argument.
     * The default behavior of {@code handleIOException(e)} is to re-throw {@code e},
     * and in Java, throwing a null reference results in an NPE.
     */
    @Test(expected = NullPointerException.class)
    public void handleIOExceptionWithNullArgumentThrowsNullPointerException() throws IOException {
        // Arrange: A concrete implementation of ProxyReader is needed to test its protected method.
        // CloseShieldReader is a suitable choice. The underlying reader is not used in this test.
        final Reader dummyReader = new PipedReader();
        final ProxyReader proxyReader = CloseShieldReader.wrap(dummyReader);

        // Act: This call is expected to throw a NullPointerException because the default
        // implementation attempts to re-throw the provided null exception.
        proxyReader.handleIOException(null);

        // Assert: The test passes if the expected NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}