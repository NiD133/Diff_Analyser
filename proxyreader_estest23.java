package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Unit tests for the {@link ProxyReader} class.
 */
public class ProxyReaderTest {

    /**
     * Tests that a call to read(char[], int, int) with parameters that are out of
     * bounds for the destination buffer correctly throws an IndexOutOfBoundsException.
     * This test verifies that the exception from the underlying (proxied) reader
     * is properly propagated.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferParametersThrowsIndexOutOfBoundsException() throws IOException {
        // Arrange: Set up the proxy reader and test parameters.
        // We use CloseShieldReader as a concrete implementation of the abstract ProxyReader.
        final Reader sourceReader = new StringReader("any-data");
        final ProxyReader proxyReader = CloseShieldReader.wrap(sourceReader);

        final char[] buffer = new char[0];
        // Define offset and length values that are intentionally out of bounds for the empty buffer.
        final int invalidOffset = 49;
        final int invalidLength = 49;

        // Act: Attempt to read using the invalid parameters.
        // This call should be delegated to the underlying StringReader, which will
        // throw the IndexOutOfBoundsException.
        proxyReader.read(buffer, invalidOffset, invalidLength);

        // Assert: The test is successful if an IndexOutOfBoundsException is thrown,
        // which is handled by the @Test(expected) annotation.
    }
}