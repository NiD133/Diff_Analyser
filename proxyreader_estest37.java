package org.apache.commons.io.input;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for the {@link ProxyReader} class.
 */
public class ProxyReaderTest {

    /**
     * Tests that the default implementation of {@code handleIOException} re-throws
     * the exact exception instance it was given.
     */
    @Test
    public void handleIOExceptionShouldRethrowGivenException() {
        // Arrange
        final IOException originalException = new IOException("Test exception");
        
        // We need a concrete instance of the abstract ProxyReader.
        // CloseShieldReader is a simple choice. The underlying reader is not used.
        final ProxyReader proxyReader = new CloseShieldReader(new StringReader(""));

        // Act & Assert
        try {
            proxyReader.handleIOException(originalException);
            fail("Expected an IOException to be thrown.");
        } catch (final IOException thrownException) {
            // Verify that the *exact same* exception object was re-thrown.
            assertSame("The thrown exception should be the same instance as the original.",
                    originalException, thrownException);
        }
    }
}