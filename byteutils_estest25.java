package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that calling {@link ByteUtils#fromLittleEndian(byte[])} with a byte array
     * longer than 8 bytes throws an {@link IllegalArgumentException}, as a long
     * can only store up to 8 bytes of data.
     */
    @Test
    public void fromLittleEndianWithTooLongArrayThrowsIllegalArgumentException() {
        // Arrange: A long is 8 bytes, so an array of 9 bytes is too large to convert.
        final byte[] inputArray = new byte[9];
        final String expectedMessage = "Can't read more than eight bytes into a long value";

        // Act & Assert: Call the method and verify the correct exception is thrown.
        // The assertThrows method (from JUnit 4.13+) is a modern and clear way
        // to test for expected exceptions.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> ByteUtils.fromLittleEndian(inputArray)
        );

        // Assert: Verify the exception message is as expected to ensure the
        // exception was thrown for the correct reason.
        assertEquals(expectedMessage, thrown.getMessage());
    }
}