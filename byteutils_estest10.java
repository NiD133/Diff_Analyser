package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link ByteUtils} class, focusing on the fromLittleEndian method.
 */
public class ByteUtilsTest {

    /**
     * Verifies that fromLittleEndian correctly constructs a long value from an 8-byte sequence
     * provided by a ByteSupplier in little-endian order.
     */
    @Test
    public void fromLittleEndian_givenByteSupplier_buildsCorrectLongFromEightBytes() throws IOException {
        // Arrange
        // We want to construct the 8-byte long value 0x0807060504030201L.
        // In little-endian format, the bytes are supplied from the least significant (LSB)
        // to the most significant (MSB).
        final long expectedLong = 0x0807060504030201L;
        final int length = 8;

        ByteUtils.ByteSupplier mockSupplier = mock(ByteUtils.ByteSupplier.class);

        // Stub the supplier to return the bytes for expectedLong in little-endian order.
        // LSB (0x01) is returned first, MSB (0x08) is returned last.
        when(mockSupplier.getAsByte()).thenReturn(
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
        );

        // Act
        long actualLong = ByteUtils.fromLittleEndian(mockSupplier, length);

        // Assert
        assertEquals(expectedLong, actualLong);
    }
}