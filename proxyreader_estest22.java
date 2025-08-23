package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Unit tests for the abstract {@link ProxyReader} class, testing its delegation behavior.
 */
public class ProxyReaderTest {

    /**
     * Tests that a call to read() with a null buffer is correctly proxied
     * and results in a NullPointerException from the underlying reader.
     */
    @Test(expected = NullPointerException.class)
    public void readWithNullBufferShouldThrowNullPointerException() throws IOException {
        // Arrange
        // A CloseShieldReader is used as a concrete implementation of the abstract ProxyReader.
        Reader underlyingReader = new StringReader("test-data");
        ProxyReader proxyReader = CloseShieldReader.wrap(underlyingReader);

        // Act
        // This call should be delegated to the underlying StringReader,
        // which will throw the NullPointerException.
        proxyReader.read(null, 0, 10);

        // Assert: The 'expected' attribute on the @Test annotation handles the assertion.
        // The test will fail if a NullPointerException is not thrown.
    }
}