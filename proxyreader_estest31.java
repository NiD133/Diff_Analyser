package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedReader;
import org.junit.Test;

/**
 * This test class contains an improved version of a test for ProxyReader.
 * Note: The original class name and inheritance are preserved for context,
 * but in a typical project, this test would be part of a class like "ProxyReaderTest".
 */
public class ProxyReader_ESTestTest31 extends ProxyReader_ESTest_scaffolding {

    /**
     * Tests that an IOException thrown by the underlying (proxied) reader
     * is correctly propagated by the ProxyReader.
     */
    @Test
    public void readShouldPropagateIOExceptionFromUnderlyingReader() {
        // ARRANGE: Create a reader that is guaranteed to throw an IOException on read.
        // A PipedReader that is not connected to a PipedWriter serves this purpose.
        final PipedReader failingReader = new PipedReader();
        
        // TaggedReader is a concrete subclass of the abstract ProxyReader, suitable for testing.
        final ProxyReader proxyReader = new TaggedReader(failingReader);

        // ACT & ASSERT: Verify that calling read() on the proxy propagates the expected exception.
        try {
            proxyReader.read();
            fail("Expected an IOException to be thrown because the underlying pipe is not connected.");
        } catch (final IOException e) {
            // The test is successful if an IOException is caught.
            // For a more specific test, we also verify the exception's message,
            // which comes from the underlying PipedReader implementation.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}