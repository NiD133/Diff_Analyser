package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedReader;

/**
 * Tests for {@link ProxyReader}.
 * This test focuses on ensuring that IOExceptions from the underlying reader are correctly propagated.
 */
public class ProxyReaderTest { // Renamed from the generated ProxyReader_ESTestTest24

    @Test
    public void readShouldPropagateIOExceptionFromUnderlyingReader() {
        // Arrange: Create a proxy reader wrapping a PipedReader that is known to
        // throw an IOException on read because it is not connected to a PipedWriter.
        PipedReader unconnectedReader = new PipedReader();
        ProxyReader proxy = CloseShieldReader.wrap(unconnectedReader);
        char[] dummyBuffer = new char[10];

        // Act & Assert
        try {
            proxy.read(dummyBuffer, 0, dummyBuffer.length);
            fail("An IOException should have been thrown because the underlying reader is not connected.");
        } catch (final IOException e) {
            // Verify that the expected exception was thrown with the correct message from the underlying reader.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}