package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.primitives.SignedBytes;
import java.util.Comparator;

/**
 * Test suite for SignedBytes utility class.
 * Tests cover casting, comparison, min/max operations, sorting, and string joining functionality.
 */
public class SignedBytesTest {

    // Test constants for better readability
    private static final byte MIN_BYTE = -128;
    private static final byte MAX_BYTE = 127;
    private static final long OUT_OF_RANGE_POSITIVE = 1940L;
    private static final long OUT_OF_RANGE_NEGATIVE = -1826L;
    private static final long UNDERFLOW_VALUE = -2776L;
    private static final long OVERFLOW_VALUE = 209L;

    // ========== Casting Tests ==========

    @Test
    public void saturatedCast_withMinByteValue_returnsMinByte() {
        byte result = SignedBytes.saturatedCast(MIN_BYTE);
        assertEquals(MIN_BYTE, result);
    }

    @Test
    public void saturatedCast_withMaxByteValue_returnsMaxByte() {
        byte result = SignedBytes.saturatedCast(127L);
        assertEquals(MAX_BYTE, result);
    }

    @Test
    public void saturatedCast_withZero_returnsZero() {
        byte result = SignedBytes.saturatedCast(0L);
        assertEquals((byte) 0, result);
    }

    @Test
    public void saturatedCast_withUnderflowValue_returnsMinByte() {
        byte result = SignedBytes.saturatedCast(UNDERFLOW_VALUE);
        assertEquals(MIN_BYTE, result);
    }

    @Test
    public void saturatedCast_withOverflowValue_returnsMaxByte() {
        byte result = SignedBytes.saturatedCast(OVERFLOW_VALUE);
        assertEquals(MAX_BYTE, result);
    }

    @Test
    public void checkedCast_withValidByteValue_returnsValue() {
        byte result = SignedBytes.checkedCast((byte) 39);
        assertEquals((byte) 39, result);
    }

    @Test
    public void checkedCast_withZero_returnsZero() {
        byte result = SignedBytes.checkedCast(0L);
        assertEquals((byte) 0, result);
    }

    @Test
    public void checkedCast_withNegativeOne_returnsNegativeOne() {
        byte result = SignedBytes.checkedCast(-1L);
        assertEquals((byte) -1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkedCast_withPositiveOutOfRangeValue_throwsException() {
        SignedBytes.checkedCast(OUT_OF_RANGE_POSITIVE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkedCast_withNegativeOutOfRangeValue_throwsException() {
        SignedBytes.checkedCast(OUT_OF_RANGE_NEGATIVE);
    }

    // ========== Comparison Tests ==========

    @Test
    public void compare_withEqualValues_returnsZero() {
        int result = SignedBytes.compare((byte) 0, (byte) 0);
        assertEquals(0, result);
    }

    @Test
    public void compare_withFirstValueGreater_returnsPositive() {
        int result = SignedBytes.compare((byte) 111, (byte) 0);
        assertEquals(111, result);
    }

    @Test
    public void compare_withFirstValueSmaller_returnsNegative() {
        int result = SignedBytes.compare((byte) 0, (byte) 84);
        assertEquals(-84, result);
    }

    // ========== Min/Max Tests ==========

    @Test
    public void min_withMixedPositiveValues_returnsSmallest() {
        byte[] array = {39, 110, 54};
        byte result = SignedBytes.min(array);
        assertEquals((byte) 39, result);
    }

    @Test
    public void min_withNegativeValue_returnsNegativeValue() {
        byte[] array = {0, -69};
        byte result = SignedBytes.min(array);
        assertEquals((byte) -69, result);
    }

    @Test
    public void min_withMixedValues_returnsSmallest() {
        byte[] array = {16, 0, 0, 0, 0, 0, 0, 0};
        byte result = SignedBytes.min(array);
        assertEquals((byte) 0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void min_withEmptyArray_throwsException() {
        SignedBytes.min(new byte[0]);
    }

    @Test(expected = NullPointerException.class)
    public void min_withNullArray_throwsException() {
        SignedBytes.min((byte[]) null);
    }

    @Test
    public void max_withSingleNegativeValue_returnsValue() {
        byte[] array = {-81};
        byte result = SignedBytes.max(array);
        assertEquals((byte) -81, result);
    }

    @Test
    public void max_withMixedValues_returnsLargest() {
        byte[] array = {0, 110, 0};
        byte result = SignedBytes.max(array);
        assertEquals((byte) 110, result);
    }

    @Test
    public void max_withAllZeros_returnsZero() {
        byte[] array = {0, 0, 0};
        byte result = SignedBytes.max(array);
        assertEquals((byte) 0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void max_withEmptyArray_throwsException() {
        SignedBytes.max(new byte[0]);
    }

    @Test(expected = NullPointerException.class)
    public void max_withNullArray_throwsException() {
        SignedBytes.max((byte[]) null);
    }

    // ========== Sorting Tests ==========

    @Test
    public void sortDescending_withAllZeros_maintainsArray() {
        byte[] array = new byte[3];
        SignedBytes.sortDescending(array);
        assertArrayEquals(new byte[]{0, 0, 0}, array);
    }

    @Test
    public void sortDescending_withSubrange_sortsOnlySubrange() {
        byte[] array = new byte[6];
        SignedBytes.sortDescending(array, 0, 1);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0}, array);
    }

    @Test(expected = NullPointerException.class)
    public void sortDescending_withNullArray_throwsException() {
        SignedBytes.sortDescending((byte[]) null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void sortDescending_withInvalidRange_throwsException() {
        byte[] array = new byte[13];
        SignedBytes.sortDescending(array, 127, 127);
    }

    // ========== String Joining Tests ==========

    @Test
    public void join_withEmptyArray_returnsEmptyString() {
        String result = SignedBytes.join("1", new byte[0]);
        assertEquals("", result);
    }

    @Test
    public void join_withMultipleZeros_joinsWithSeparator() {
        byte[] array = new byte[3]; // all zeros
        String result = SignedBytes.join("&#GMks!-I`k", array);
        assertEquals("0&#GMks!-I`k0&#GMks!-I`k0", result);
    }

    @Test(expected = NullPointerException.class)
    public void join_withNullArray_throwsException() {
        SignedBytes.join("Out of range: %s", (byte[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void join_withNullSeparator_throwsException() {
        SignedBytes.join((String) null, new byte[0]);
    }

    // ========== Comparator Tests ==========

    @Test
    public void lexicographicalComparator_returnsNonNullComparator() {
        Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
        assertNotNull(comparator);
    }
}