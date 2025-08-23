package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(InputStream, int)} throws an
     * IllegalArgumentException if the requested length is greater than 8, which is
     * the size of a long.
     */
    @Test
    void fromLittleEndianFromStreamShouldThrowExceptionForLengthGreaterThanEight() {
        // Arrange: The maximum valid length is 8. We use 9 to test the boundary condition.
        final int invalidLength = 9;
        final InputStream emptyStream = new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY);

        // Act & Assert: Verify that calling the method with an invalid length throws an exception.
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ByteUtils.fromLittleEndian(emptyStream, invalidLength);
        });

        // Further assert on the exception message for a more specific test.
        assertEquals("length must not be greater than 8.", exception.getMessage());
    }
}