package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.primitives.SignedBytes;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SignedBytesTest extends SignedBytes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSaturatedCastWithinByteRange() {
        byte result = SignedBytes.saturatedCast((byte) (-128));
        assertEquals((byte) (-128), result);
    }

    @Test(timeout = 4000)
    public void testSaturatedCastAtUpperByteLimit() {
        byte result = SignedBytes.saturatedCast(127L);
        assertEquals((byte) 127, result);
    }

    @Test(timeout = 4000)
    public void testCheckedCastThrowsExceptionWhenOutOfRange() {
        try {
            SignedBytes.checkedCast(1940L);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testSortDescendingSingleElement() {
        byte[] byteArray = new byte[6];
        SignedBytes.sortDescending(byteArray, 0, 1);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0}, byteArray);
    }

    @Test(timeout = 4000)
    public void testSaturatedCastZero() {
        byte result = SignedBytes.saturatedCast(0L);
        assertEquals((byte) 0, result);
    }

    @Test(timeout = 4000)
    public void testMinValueInArray() {
        byte[] byteArray = {(byte) 39, (byte) 110, (byte) 54};
        byte result = SignedBytes.min(byteArray);
        assertEquals((byte) 39, result);
    }

    @Test(timeout = 4000)
    public void testMinValueInArrayWithNegative() {
        byte[] byteArray = {0, (byte) -69};
        byte result = SignedBytes.min(byteArray);
        assertEquals((byte) -69, result);
    }

    @Test(timeout = 4000)
    public void testMaxValueInSingleElementArray() {
        byte[] byteArray = {(byte) -81};
        byte result = SignedBytes.max(byteArray);
        assertEquals((byte) -81, result);
    }

    @Test(timeout = 4000)
    public void testCompareEqualBytes() {
        int result = SignedBytes.compare((byte) 0, (byte) 0);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testCompareDifferentBytes() {
        int result = SignedBytes.compare((byte) 111, (byte) 0);
        assertEquals(111, result);
    }

    @Test(timeout = 4000)
    public void testCheckedCastZero() {
        byte result = SignedBytes.checkedCast(0L);
        assertEquals((byte) 0, result);
    }

    @Test(timeout = 4000)
    public void testCheckedCastNegativeOne() {
        byte result = SignedBytes.checkedCast(-1L);
        assertEquals((byte) -1, result);
    }

    @Test(timeout = 4000)
    public void testSortDescendingNullArrayThrowsException() {
        try {
            SignedBytes.sortDescending(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinNullArrayThrowsException() {
        try {
            SignedBytes.min(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.primitives.SignedBytes", e);
        }
    }

    @Test(timeout = 4000)
    public void testMaxNullArrayThrowsException() {
        try {
            SignedBytes.max(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.primitives.SignedBytes", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinNullArrayThrowsException() {
        try {
            SignedBytes.join("Out of range: %s", null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.primitives.SignedBytes", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinNullSeparatorThrowsException() {
        byte[] byteArray = new byte[0];
        try {
            SignedBytes.join(null, byteArray);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testSortDescendingInvalidRangeThrowsException() {
        byte[] byteArray = new byte[13];
        try {
            SignedBytes.sortDescending(byteArray, 127, 127);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testJoinEmptyArray() {
        byte[] byteArray = new byte[0];
        String result = SignedBytes.join("1", byteArray);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testJoinArrayWithSeparator() {
        byte[] byteArray = new byte[3];
        String result = SignedBytes.join("&#GMks!-I`k", byteArray);
        assertEquals("0&#GMks!-I`k0&#GMks!-I`k0", result);
    }

    @Test(timeout = 4000)
    public void testMaxValueInArray() {
        byte[] byteArray = {0, (byte) 110, 0};
        byte result = SignedBytes.max(byteArray);
        assertEquals((byte) 110, result);
    }

    @Test(timeout = 4000)
    public void testMaxValueInEmptyArrayThrowsException() {
        byte[] byteArray = new byte[0];
        try {
            SignedBytes.max(byteArray);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinValueInEmptyArrayThrowsException() {
        byte[] byteArray = new byte[0];
        try {
            SignedBytes.min(byteArray);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testSaturatedCastBelowByteRange() {
        byte result = SignedBytes.saturatedCast(-2776L);
        assertEquals((byte) -128, result);
    }

    @Test(timeout = 4000)
    public void testSaturatedCastAboveByteRange() {
        byte result = SignedBytes.saturatedCast(209L);
        assertEquals((byte) 127, result);
    }

    @Test(timeout = 4000)
    public void testCheckedCastWithinByteRange() {
        byte result = SignedBytes.checkedCast((byte) 39);
        assertEquals((byte) 39, result);
    }

    @Test(timeout = 4000)
    public void testCheckedCastThrowsExceptionWhenOutOfRangeNegative() {
        try {
            SignedBytes.checkedCast(-1826L);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testSortDescendingArray() {
        byte[] byteArray = new byte[3];
        SignedBytes.sortDescending(byteArray);
        assertEquals(3, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testCompareBytesDifferentOrder() {
        int result = SignedBytes.compare((byte) 0, (byte) 84);
        assertEquals(-84, result);
    }

    @Test(timeout = 4000)
    public void testLexicographicalComparatorNotNull() {
        Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
        assertNotNull(comparator);
    }
}