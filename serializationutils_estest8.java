package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedOutputStream;

import org.junit.Test;

/**
 * Tests for {@link SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that attempting to serialize to an unconnected PipedOutputStream
     * results in a SerializationException that wraps the underlying IOException.
     */
    @Test
    public void testSerializeToUnconnectedPipeThrowsException() {
        // Arrange: Create a serializable object and an output stream that will fail on write.
        // A PipedOutputStream must be connected to a PipedInputStream before use.
        final PipedOutputStream unconnectedOutputStream = new PipedOutputStream();
        final String objectToSerialize = "some test data";

        // Act & Assert: Attempt to serialize and verify the correct exception is thrown.
        try {
            SerializationUtils.serialize(objectToSerialize, unconnectedOutputStream);
            fail("Expected SerializationException for writing to an unconnected pipe.");
        } catch (final SerializationException e) {
            // The method should wrap the underlying IOException in a SerializationException.
            final Throwable cause = e.getCause();
            assertNotNull("The exception should have a cause.", cause);
            assertTrue("The cause should be an instance of IOException.", cause instanceof IOException);
            assertEquals("The cause message should indicate the pipe is not connected.",
                         "Pipe not connected", cause.getMessage());
        }
    }
}