package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for the {@link ProxyReader} class.
 * This test focuses on the default behavior of the handleIOException method.
 */
// In a real-world scenario, this class would be named ProxyReaderTest.
// The original EvoSuite-generated name is kept here for context.
public class ProxyReader_ESTestTest35 {

    /**
     * Verifies that the default implementation of handleIOException
     * re-throws the exact exception instance it was given.
     */
    @Test
    public void handleIOExceptionShouldRethrowTheGivenException() {
        // Arrange: Set up the test objects.
        // We need a concrete subclass of the abstract ProxyReader. TaggedReader works well.
        // A simple StringReader is sufficient as the underlying reader.
        Reader proxyReader = new TaggedReader(new StringReader(""));

        // The specific exception instance we expect to be re-thrown.
        final IOException originalException = new IOException("Test Exception");

        // Act & Assert: Call the method and verify the outcome.
        try {
            // The handleIOException method is protected, but we can call it
            // through our TaggedReader instance.
            ((ProxyReader) proxyReader).handleIOException(originalException);
            fail("Expected an IOException to be thrown.");
        } catch (final IOException thrownException) {
            // Assert that the caught exception is the *same instance* as the one we passed in,
            // not just an equivalent one. This confirms the default "re-throw" behavior.
            assertSame("The re-thrown exception should be the same instance as the original.",
                    originalException, thrownException);
        }
    }
}