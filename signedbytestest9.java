package com.google.common.primitives;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.testing.Helpers;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link SignedBytes#lexicographicalComparator()}.
 */
@RunWith(JUnit4.class)
public class SignedBytesLexicographicalComparatorTest {

    private static final byte MIN_VALUE = Byte.MIN_VALUE; // -128
    private static final byte MAX_VALUE = Byte.MAX_VALUE; // 127

    // The constants are prefixed with letters to visually represent their lexicographical order.
    private static final byte[] A_EMPTY = {};
    private static final byte[] B_MIN_VALUE = {MIN_VALUE};
    private static final byte[] C_MIN_VALUE_PREFIX = {MIN_VALUE, MIN_VALUE};
    private static final byte[] D_MIN_VALUE_THEN_POSITIVE = {MIN_VALUE, 1};
    private static final byte[] E_POSITIVE_ONE = {1};
    private static final byte[] F_POSITIVE_THEN_MIN_VALUE = {1, MIN_VALUE};
    private static final byte[] G_MAX_ALMOST = {MAX_VALUE, (byte) (MAX_VALUE - 1)};
    private static final byte[] H_MAX_EXACT = {MAX_VALUE, MAX_VALUE};
    private static final byte[] I_MAX_LONGER = {MAX_VALUE, MAX_VALUE, MAX_VALUE};

    /**
     * Verifies that the lexicographical comparator correctly orders a list of byte arrays. The test
     * cases include:
     * <ul>
     *   <li>Empty vs. non-empty arrays.
     *   <li>Arrays where one is a prefix of the other.
     *   <li>Arrays of the same length with different values.
     *   <li>Arrays containing minimum and maximum byte values.
     * </ul>
     */
    @Test
    public void lexicographicalComparator_sortsArraysAsExpected() {
        List<byte[]> orderedExamples =
            Arrays.asList(
                A_EMPTY,
                B_MIN_VALUE,
                C_MIN_VALUE_PREFIX,
                D_MIN_VALUE_THEN_POSITIVE,
                E_POSITIVE_ONE,
                F_POSITIVE_THEN_MIN_VALUE,
                G_MAX_ALMOST,
                H_MAX_EXACT,
                I_MAX_LONGER);

        Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();

        // Helpers.testComparator verifies that the comparator imposes the same order
        // as the list's iteration order.
        Helpers.testComparator(comparator, orderedExamples);
    }
}