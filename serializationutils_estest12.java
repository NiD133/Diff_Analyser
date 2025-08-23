package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import org.junit.Test;

/**
 * Test suite for the exception handling in {@link SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that calling deserialize with a stream that throws an IOException
     * correctly wraps the original exception in a SerializationException.
     */
    @Test
    public void deserializeWithFailingStreamShouldThrowSerializationException() {
        // Arrange: A PipedInputStream that is not connected to a PipedOutputStream
        // will throw an IOException ("Pipe not connected") upon any read attempt.
        // This simulates any failing input stream.
        final InputStream failingStream = new PipedInputStream();

        // Act & Assert
        try {
            SerializationUtils.deserialize(failingStream);
            fail("Expected a SerializationException to be thrown due to the underlying IOException.");
        } catch (final SerializationException e) {
            // The method should wrap the underlying IOException in a SerializationException.
            final Throwable cause = e.getCause();

            assertNotNull("The exception should have a cause.", cause);
            assertTrue("The cause should be an instance of IOException.", cause instanceof IOException);
            assertEquals("The cause message should indicate a pipe connection issue.",
                         "Pipe not connected", cause.getMessage());
        }
    }
}