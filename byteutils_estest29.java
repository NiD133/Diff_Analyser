package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(InputStream, int)} throws an
     * IllegalArgumentException if the requested read length is greater than 8,
     * which is the maximum size of a long.
     */
    @Test
    public void fromLittleEndianWithInputStreamShouldThrowExceptionForLengthGreaterThanEight() throws IOException {
        // Arrange: A long can consist of at most 8 bytes. We use 9 to test the boundary condition.
        final int invalidLength = 9;
        final String expectedMessage = "Can't read more than eight bytes into a long value";

        // Act & Assert
        try {
            // The InputStream can be null because the length validation occurs before any stream access.
            ByteUtils.fromLittleEndian((InputStream) null, invalidLength);
            fail("Expected an IllegalArgumentException because the length is greater than 8.");
        } catch (final IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}