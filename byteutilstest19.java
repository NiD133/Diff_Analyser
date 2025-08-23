package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
class ByteUtilsTest {

    @Test
    void toLittleEndianShouldWriteValueToByteArraySlice() {
        // Arrange
        // The value 262914 requires 3 bytes and is represented in little-endian
        // format as {2, 3, 4}.
        // Calculation: 262914 = 2 * (256^0) + 3 * (256^1) + 4 * (256^2)
        final long valueToWrite = 262914L;
        final int offset = 1;
        final int length = 3;

        // The target buffer is larger than the written part to ensure the offset is respected
        // and that bytes outside the target range are not modified.
        final byte[] actualBuffer = new byte[4];
        final byte[] expectedBuffer = {0, 2, 3, 4};

        // Act
        toLittleEndian(actualBuffer, valueToWrite, offset, length);

        // Assert
        assertArrayEquals(expectedBuffer, actualBuffer,
            "The little-endian value should be written correctly at the specified offset.");
    }
}