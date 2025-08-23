package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
class ByteUtilsTest {

    @Test
    void fromLittleEndianWithOffsetShouldReadUnsigned32BitValue() {
        // Arrange
        // The relevant bytes {0x02, 0x03, 0x04, 0x80} are used to form the little-endian value.
        // The first byte (0x01) is ignored due to the offset, testing the offset functionality.
        final byte[] sourceBytes = {0x01, 0x02, 0x03, 0x04, (byte) 0x80};
        final int offset = 1;
        final int length = 4;

        // The little-endian bytes {0x02, 0x03, 0x04, 0x80} represent the number 0x80040302.
        // This value (2,147,746,818) is larger than Integer.MAX_VALUE, which ensures
        // that the method correctly handles values that would be negative if treated
        // as a signed 32-bit integer.
        final long expectedValue = 0x80040302L;

        // Act
        final long actualValue = fromLittleEndian(sourceBytes, offset, length);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}