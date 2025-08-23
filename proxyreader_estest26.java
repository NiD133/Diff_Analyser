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
     * Tests that calling read(char[]) with a null buffer correctly throws a
     * NullPointerException. The ProxyReader should delegate the call to the
     * underlying reader, which is the source of the expected exception.
     */
    @Test(expected = NullPointerException.class)
    public void readWithNullCharArrayShouldThrowNullPointerException() throws IOException {
        // Arrange: Create a proxy reader instance for the test.
        // CloseShieldReader is a concrete subclass of ProxyReader.
        Reader proxyReader = CloseShieldReader.wrap(new PipedReader());

        // Act: Attempt to read from the stream into a null buffer.
        // This action is expected to throw the exception.
        proxyReader.read((char[]) null);

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}