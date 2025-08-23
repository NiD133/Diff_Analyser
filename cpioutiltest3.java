package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the CpioUtil class.
 */
class CpioUtilTest {

    @Test
    @DisplayName("long2byteArray() should throw an exception for a zero length")
    void long2byteArrayShouldThrowExceptionForZeroLength() {
        // According to its Javadoc, long2byteArray requires the length to be a positive multiple of two.
        // This test verifies that a non-positive length (zero) correctly throws an exception.
        assertThrows(UnsupportedOperationException.class, () -> CpioUtil.long2byteArray(0L, 0, false));
    }
}