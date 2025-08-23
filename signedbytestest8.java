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

public class SignedBytesTestTest8 extends TestCase {

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

    public void testJoin() {
        assertThat(SignedBytes.join(",", EMPTY)).isEmpty();
        assertThat(SignedBytes.join(",", ARRAY1)).isEqualTo("1");
        assertThat(SignedBytes.join(",", (byte) 1, (byte) 2)).isEqualTo("1,2");
        assertThat(SignedBytes.join("", (byte) 1, (byte) 2, (byte) 3)).isEqualTo("123");
        assertThat(SignedBytes.join(",", (byte) -128, (byte) -1)).isEqualTo("-128,-1");
    }
}
