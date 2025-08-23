package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Readable tests for SegmentUtils.
 * 
 * The tests focus on:
 * - Argument counting from JVM-style method descriptors
 * - Bit-16 flag counting across int/long/2D-long arrays
 * - Matching counts over arrays via IMatcher
 * - Robustness for null/invalid inputs
 */
public class SegmentUtilsTest {

    // -------------------------
    // countArgs (descriptor)
    // -------------------------

    @Test
    public void countArgs_zeroArgs_returnsZero() {
        assertEquals(0, SegmentUtils.countArgs("()V"));
    }

    @Test
    public void countArgs_countsPrimitivesAndArrays_withWidth1() {
        // (I D J Ljava/lang/String; [I [[D) -> 6 parameters for width=1
        assertEquals(6, SegmentUtils.countArgs("(IDJLjava/lang/String;[I[[D)V"));
    }

    @Test
    public void countArgs_nullDescriptor_throwsNPE() {
        assertThrows(NullPointerException.class, () -> SegmentUtils.countArgs(null));
    }

    @Test
    public void countArgs_invalidDescriptor_throwsIAE() {
        // Missing '(' ... ')' structure
        assertThrows(IllegalArgumentException.class, () -> SegmentUtils.countArgs("not-a-descriptor"));
    }

    // -------------------------
    // countArgs (descriptor, widthOfLongsAndDoubles)
    // -------------------------

    @Test
    public void countArgs_customWidth_countsLongAndDoubleUsingGivenWidth() {
        // Descriptor with J (long) and D (double)
        // With width=3, expect 3 + 3 = 6
        assertEquals(6, SegmentUtils.countArgs("(JD)V", 3));
    }

    @Test
    public void countArgs_customWidth_negativeWidth_isAppliedAsGiven() {
        // With width=-1, long + double => -2
        assertEquals(-2, SegmentUtils.countArgs("(JD)V", -1));
    }

    @Test
    public void countArgs_customWidth_nullDescriptor_throwsNPE() {
        assertThrows(NullPointerException.class, () -> SegmentUtils.countArgs(null, 2));
    }

    @Test
    public void countArgs_customWidth_invalidDescriptor_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> SegmentUtils.countArgs("bad", 2));
    }

    // -------------------------
    // countInvokeInterfaceArgs
    // -------------------------

    @Test
    public void countInvokeInterfaceArgs_countsLongAndDoubleAsTwo() {
        // J (2) + I (1) => 3
        assertEquals(3, SegmentUtils.countInvokeInterfaceArgs("(JI)V"));
    }

    @Test
    public void countInvokeInterfaceArgs_emptyArgs_returnsZero() {
        assertEquals(0, SegmentUtils.countInvokeInterfaceArgs("()V"));
    }

    @Test
    public void countInvokeInterfaceArgs_nullDescriptor_throwsNPE() {
        assertThrows(NullPointerException.class, () -> SegmentUtils.countInvokeInterfaceArgs(null));
    }

    @Test
    public void countInvokeInterfaceArgs_invalidDescriptor_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> SegmentUtils.countInvokeInterfaceArgs("invalid"));
    }

    // -------------------------
    // countBit16 (int[])
    // -------------------------

    @Test
    public void countBit16_intArray_countsElementsWithBit16Set() {
        // Elements with bit 16 set: (1<<16) and -1
        final int[] flags = {0, 1, (1 << 16), (1 << 17), -1};
        assertEquals(2, SegmentUtils.countBit16(flags));
    }

    @Test
    public void countBit16_intArray_empty_returnsZero() {
        assertEquals(0, SegmentUtils.countBit16(new int[0]));
    }

    @Test
    public void countBit16_intArray_null_throwsNPE() {
        assertThrows(NullPointerException.class, () -> SegmentUtils.countBit16((int[]) null));
    }

    // -------------------------
    // countBit16 (long[])
    // -------------------------

    @Test
    public void countBit16_longArray_countsElementsWithBit16Set() {
        final long[] flags = {0L, 65536L, 65535L, -1L};
        // 65536 (bit 16 set) and -1 (all bits set)
        assertEquals(2, SegmentUtils.countBit16(flags));
    }

    @Test
    public void countBit16_longArray_empty_returnsZero() {
        assertEquals(0, SegmentUtils.countBit16(new long[0]));
    }

    @Test
    public void countBit16_longArray_null_throwsNPE() {
        assertThrows(NullPointerException.class, () -> SegmentUtils.countBit16((long[]) null));
    }

    // -------------------------
    // countBit16 (long[][])
    // -------------------------

    @Test
    public void countBit16_long2D_countsElementsWithBit16Set() {
        final long[][] flags = {
                {0L, 65536L},
                {},
                {-1L}
        };
        // 65536 and -1 contribute => 2
        assertEquals(2, SegmentUtils.countBit16(flags));
    }

    @Test
    public void countBit16_long2D_emptyTopLevel_returnsZero() {
        assertEquals(0, SegmentUtils.countBit16(new long[0][0]));
    }

    @Test
    public void countBit16_long2D_null_throwsNPE() {
        assertThrows(NullPointerException.class, () -> SegmentUtils.countBit16((long[][]) null));
    }

    // -------------------------
    // countMatches (long[], IMatcher)
    // -------------------------

    @Test
    public void countMatches_1D_withMatcher_countsMatches() {
        final long[] values = {1, 2, 4, 5};

        final IMatcher evenMatcher = new IMatcher() {
            @Override
            public boolean matches(final long value) {
                return (value & 1L) == 0L;
            }
        };

        assertEquals(2, SegmentUtils.countMatches(values, evenMatcher)); // 2 and 4
    }

    @Test
    public void countMatches_1D_nullMatcherAndEmptyArray_returnsZero() {
        assertEquals(0, SegmentUtils.countMatches(new long[0], null));
    }

    @Test
    public void countMatches_1D_nullMatcherAndNonEmptyArray_throwsNPE() {
        assertThrows(NullPointerException.class, () -> SegmentUtils.countMatches(new long[]{1L}, null));
    }

    // -------------------------
    // countMatches (long[][], IMatcher)
    // -------------------------

    @Test
    public void countMatches_2D_withMatcher_countsMatches() {
        final long[][] values = {
                {1L, 2L},
                {},
                {4L, 5L}
        };

        final IMatcher evenMatcher = new IMatcher() {
            @Override
            public boolean matches(final long value) {
                return (value & 1L) == 0L;
            }
        };

        assertEquals(2, SegmentUtils.countMatches(values, evenMatcher)); // 2 and 4
    }

    @Test
    public void countMatches_2D_nullMatcherAndEmptyTopLevel_returnsZero() {
        assertEquals(0, SegmentUtils.countMatches(new long[0][0], null));
    }

    @Test
    public void countMatches_2D_nullMatcherAndNonEmpty_throwsNPE() {
        // Non-empty 2D content -> dereferencing null matcher should fail
        final long[][] values = { {1L}, {}, {2L} };
        assertThrows(NullPointerException.class, () -> SegmentUtils.countMatches(values, null));
    }
}