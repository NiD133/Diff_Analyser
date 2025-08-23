package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link ByteOrderMark#matches(int[])} method.
 */
@DisplayName("ByteOrderMark.matches(int[])")
public class ByteOrderMarkMatchesTest {

    private static final ByteOrderMark TEST_BOM_2_BYTES = new ByteOrderMark("TEST_BOM_2", 0x01, 0x02);

    /**
     * Provides a stream of standard and custom BOMs for parameterized testing.
     *
     * @return A stream of {@link ByteOrderMark} instances.
     */
    static Stream<ByteOrderMark> bomProvider() {
        return Stream.of(
            ByteOrderMark.UTF_8,
            ByteOrderMark.UTF_16BE,
            ByteOrderMark.UTF_16LE,
            ByteOrderMark.UTF_32BE,
            ByteOrderMark.UTF_32LE,
            new ByteOrderMark("TEST_BOM_1", 0x01)
        );
    }

    @ParameterizedTest
    @MethodSource("bomProvider")
    @DisplayName("should return true when the byte array is an exact match")
    void matchesShouldReturnTrueForExactMatch(final ByteOrderMark bom) {
        assertTrue(bom.matches(bom.getRawBytes()), "BOM should match its own bytes");
    }

    @Test
    @DisplayName("should return true when the byte array starts with the BOM")
    void matchesShouldReturnTrueForMatchingPrefix() {
        final int[] longerInput = {0x01, 0x02, 0x03, 0x04};
        assertTrue(TEST_BOM_2_BYTES.matches(longerInput), "BOM should match a longer array that starts with its bytes");
    }

    @Test
    @DisplayName("should return false when the byte array is shorter than the BOM")
    void matchesShouldReturnFalseForShorterArray() {
        final int[] shorterInput = {0x01};
        assertFalse(TEST_BOM_2_BYTES.matches(shorterInput), "BOM should not match an array shorter than itself");
    }

    @Test
    @DisplayName("should return false when the byte array has different bytes")
    void matchesShouldReturnFalseForMismatchedBytes() {
        // Mismatch in the first byte
        assertFalse(TEST_BOM_2_BYTES.matches(new int[]{0xFF, 0x02}), "Should not match with first byte different");

        // Mismatch in the second byte
        assertFalse(TEST_BOM_2_BYTES.matches(new int[]{0x01, 0xFF}), "Should not match with second byte different");
    }

    @Test
    @DisplayName("should return false for an empty byte array")
    void matchesShouldReturnFalseForEmptyArray() {
        assertFalse(TEST_BOM_2_BYTES.matches(new int[0]), "BOM should not match an empty array");
    }

    @Test
    @DisplayName("should throw NullPointerException for a null byte array")
    void matchesShouldThrowExceptionForNullInput() {
        assertThrows(NullPointerException.class, () -> TEST_BOM_2_BYTES.matches(null),
            "A null input array should cause a NullPointerException");
    }
}