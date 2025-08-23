package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the ByteOrderMark constructor throws an IllegalArgumentException
     * when the provided byte array is empty.
     */
    @Test
    public void constructorShouldThrowIllegalArgumentExceptionForEmptyByteArray() {
        // Arrange: Define a valid charset name and an empty byte array.
        final String charsetName = "UTF-8";
        final int[] emptyBytes = {};

        // Act & Assert: Attempt to create a ByteOrderMark and verify the exception.
        try {
            new ByteOrderMark(charsetName, emptyBytes);
            fail("Expected an IllegalArgumentException to be thrown for an empty byte array.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals("No bytes specified", e.getMessage());
        }
    }
}