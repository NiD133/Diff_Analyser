package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(ByteUtils.ByteSupplier, int)} throws
     * an IllegalArgumentException if the requested length is greater than 8.
     * A long value can store a maximum of 8 bytes, so any request for more bytes
     * should be rejected.
     */
    @Test
    public void fromLittleEndianWithByteSupplierShouldThrowExceptionForLengthGreaterThanEight() {
        // Arrange: A long can hold at most 8 bytes. We use 9 to test the boundary condition.
        final int invalidLength = 9;

        // The ByteSupplier is irrelevant for this test, as the length validation
        // occurs before the supplier is accessed. We can pass null.
        final ByteUtils.ByteSupplier dummySupplier = null;

        // Act & Assert: Call the method and verify that it throws the correct exception.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ByteUtils.fromLittleEndian(dummySupplier, invalidLength)
        );

        // Further Assert: Verify the exception message is as expected.
        assertEquals("Can't read more than eight bytes into a long value", exception.getMessage());
    }
}