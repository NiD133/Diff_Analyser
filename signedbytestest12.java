package com.google.common.primitives;

import static com.google.common.primitives.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.primitives.SignedBytes.max;
import static com.google.common.primitives.SignedBytes.min;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.collect.testing.Helpers;
import com.google.common.testing.NullPointerTester;
import com.google.common.testing.SerializableTester;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;

public class SignedBytesTestTest12 extends TestCase {

    private static final byte[] EMPTY = {};

    private static final byte[] ARRAY1 = { (byte) 1 };

    private static final byte LEAST = Byte.MIN_VALUE;

    private static final byte GREATEST = Byte.MAX_VALUE;

    private static final byte[] VALUES = { LEAST, -1, 0, 1, GREATEST };

    private static void assertCastFails(long value) {
        try {
            SignedBytes.checkedCast(value);
            fail("Cast to byte should have failed: " + value);
        } catch (IllegalArgumentException ex) {
            assertWithMessage(value + " not found in exception text: " + ex.getMessage()).that(ex.getMessage().contains(String.valueOf(value))).isTrue();
        }
    }

    private static void testSortDescending(byte[] input, byte[] expectedOutput) {
        input = Arrays.copyOf(input, input.length);
        SignedBytes.sortDescending(input);
        assertThat(input).isEqualTo(expectedOutput);
    }

    private static void testSortDescending(byte[] input, int fromIndex, int toIndex, byte[] expectedOutput) {
        input = Arrays.copyOf(input, input.length);
        SignedBytes.sortDescending(input, fromIndex, toIndex);
        assertThat(input).isEqualTo(expectedOutput);
    }

    public void testSortDescendingIndexed() {
        testSortDescending(new byte[] {}, 0, 0, new byte[] {});
        testSortDescending(new byte[] { 1 }, 0, 1, new byte[] { 1 });
        testSortDescending(new byte[] { 1, 2 }, 0, 2, new byte[] { 2, 1 });
        testSortDescending(new byte[] { 1, 3, 1 }, 0, 2, new byte[] { 3, 1, 1 });
        testSortDescending(new byte[] { 1, 3, 1 }, 0, 1, new byte[] { 1, 3, 1 });
        testSortDescending(new byte[] { -1, -2, 1, 2 }, 1, 3, new byte[] { -1, 1, -2, 2 });
    }
}
