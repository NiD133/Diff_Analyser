package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
// The class is renamed to follow standard naming conventions (i.e., ClassNameTest).
public class ByteUtilsTest {

    @Test
    @DisplayName("fromLittleEndian should correctly read a value from a slice of a byte array")
    void fromLittleEndianShouldReadCorrectValueFromByteArraySlice() {
        // --- Arrange ---
        // Define the input data and the slice to be read. The bytes outside the
        // slice {2, 3, 4} should be ignored by the method.
        final byte[] sourceBytes = {
            1,      // Ignored prefix byte
            2, 3, 4, // These 3 bytes form the little-endian number
            5       // Ignored suffix byte
        };
        final int offset = 1;
        final int length = 3;

        // The expected value is pre-calculated to avoid "magic numbers" in the assertion.
        // The value is read from the sub-array {2, 3, 4} in little-endian order:
        // Calculation: (2 * 256^0) + (3 * 256^1) + (4 * 256^2)
        //            = 2           + 768         + 262144
        //            = 262914
        final long expectedValue = 262914L;

        // --- Act ---
        // Call the method under test to get the actual result.
        final long actualValue = fromLittleEndian(sourceBytes, offset, length);

        // --- Assert ---
        // Verify that the actual value matches the expected value.
        // An assertion message is added to provide clear context on failure.
        assertEquals(expectedValue, actualValue,
            "The little-endian value was not read correctly from the array slice.");
    }
}