package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedReader;
import java.nio.CharBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link ProxyReader}.
 * This test case focuses on exception propagation.
 */
// The original test class name and scaffolding are preserved for context.
public class ProxyReader_ESTestTest30 extends ProxyReader_ESTest_scaffolding {

    /**
     * Tests that an IOException thrown by the underlying reader is correctly
     * propagated when calling the read(CharBuffer) method on the proxy.
     */
    @Test(timeout = 4000)
    public void readCharBufferShouldPropagateIOExceptionFromUnderlyingReader() {
        // Arrange: Create a reader that is guaranteed to throw an IOException on read.
        // A PipedReader that is not connected to a PipedWriter serves this purpose.
        final PipedReader unconnectedPipedReader = new PipedReader();
        final ProxyReader proxyReader = CloseShieldReader.wrap(unconnectedPipedReader);
        final CharBuffer buffer = CharBuffer.allocate(13);

        // Act & Assert: Verify that the expected IOException is thrown and has the correct message.
        try {
            proxyReader.read(buffer);
            fail("Expected an IOException to be thrown because the underlying reader is not connected.");
        } catch (final IOException e) {
            // The exception is expected. We now verify its message to ensure it's the correct one.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}