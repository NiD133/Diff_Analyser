package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.PipedReader;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    /**
     * Tests that {@link CharStreams#exhaust(Readable)} propagates an {@link IOException}
     * when the underlying reader fails.
     */
    @Test
    public void exhaust_onUnconnectedReader_throwsIOException() {
        // Arrange: Create a PipedReader that is not connected to a PipedWriter.
        // Any attempt to read from it will throw an IOException.
        PipedReader unconnectedReader = new PipedReader();

        // Act & Assert: Verify that calling exhaust() on this reader throws the expected IOException.
        IOException thrown = assertThrows(
            IOException.class,
            () -> CharStreams.exhaust(unconnectedReader)
        );

        // Assert: Further verify that the exception message is correct.
        assertEquals("Pipe not connected", thrown.getMessage());
    }
}