package org.apache.commons.io.input;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Tests that calling mark() on the stream throws an UnsupportedOperationException,
     * as the stream does not support mark/reset operations.
     */
    @Test
    public void markShouldThrowUnsupportedOperationException() {
        // Arrange: Create a WindowsLineEndingInputStream. The underlying stream's content
        // and the lineFeedAtEos flag are irrelevant for this test.
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(emptyStream, false);

        // Act & Assert: Verify that calling mark() throws the expected exception.
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> windowsStream.mark(0),
            "mark() should throw an UnsupportedOperationException."
        );

        // Assert: Verify the exception has the expected message.
        assertEquals("mark/reset not supported", exception.getMessage());
    }
}