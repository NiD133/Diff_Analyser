package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import java.io.IOException;
import java.io.PipedInputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link CodecEncoding}.
 *
 * Note: The original test class name "CodecEncoding_ESTestTest23" suggests it was
 * auto-generated. It has been renamed to follow standard Java conventions.
 */
public class CodecEncodingTest {

    /**
     * Verifies that getCodec() correctly propagates an IOException when the
     * underlying InputStream is unreadable.
     *
     * @throws Pack200Exception if a Pack200-specific error occurs, though not expected in this test case.
     */
    @Test
    public void getCodecShouldPropagateIOExceptionFromUnreadableStream() throws Pack200Exception {
        // Arrange
        // A PipedInputStream that is not connected to a PipedOutputStream.
        // Any attempt to read from it will throw an IOException("Pipe not connected").
        PipedInputStream unreadableStream = new PipedInputStream();

        // A codec specifier value that requires reading from the input stream.
        // According to the Pack200 specification, values >= 116 may require
        // reading additional bytes to define the codec's parameters.
        final int specifierRequiringStreamRead = 146;
        final BHSDCodec defaultCodec = Codec.UDELTA5;

        // Act & Assert
        try {
            CodecEncoding.getCodec(specifierRequiringStreamRead, unreadableStream, defaultCodec);
            fail("Expected an IOException because the input stream is not connected.");
        } catch (final IOException e) {
            // Verify that the expected exception was thrown with the correct message.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}