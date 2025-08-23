package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Unit tests for the abstract {@link ProxyReader} class.
 */
public class ProxyReaderTest {

    /**
     * Tests that the base implementation of the {@code beforeRead(int)} hook method
     * is a benign no-op. This method is designed for subclasses to override,
     * so the default behavior should not cause any errors or side effects.
     */
    @Test
    public void beforeReadShouldBeBenignNoOp() throws IOException {
        // Arrange: Create a concrete instance of the abstract ProxyReader
        // using a simple StringReader as the delegate.
        final Reader delegate = new StringReader("test data");
        final ProxyReader proxyReader = new ProxyReader(delegate) {
            // An anonymous inner class is used to instantiate the abstract
            // ProxyReader and test its base functionality directly.
        };

        // Act: Call the beforeRead method. The argument represents a request
        // to read a certain number of characters.
        proxyReader.beforeRead(10);

        // Assert: The test succeeds if no exception is thrown, confirming the
        // method is a safe, non-functional hook in the base class.
    }
}