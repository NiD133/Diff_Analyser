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
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class SignedBytes_ESTest extends SignedBytes_ESTest_scaffolding {

    // Tests for checkedCast method
    @Test(timeout = 4000)
    public void testCheckedCast_withinRange() {
        assertEquals((byte) 0, SignedBytes.checkedCast(0L));
        assertEquals((byte) -1, SignedBytes.checkedCast(-1L));
        assertEquals((byte) 39, SignedBytes.checkedCast((byte) 39));
    }

    @Test(timeout = 4000)
    public void testCheckedCast_aboveMax_throwsException() {
        try {
            SignedBytes.checkedCast(1940L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testCheckedCast_belowMin_throwsException() {
        try {
            SignedBytes.checkedCast(-1826L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Tests for saturatedCast method
    @Test(timeout = 4000)
    public void testSaturatedCast_withinRange() {
        assertEquals((byte) -128, SignedBytes.saturatedCast((byte) -128));
        assertEquals((byte) 127, SignedBytes.saturatedCast(127L));
        assertEquals((byte) 0, SignedBytes.saturatedCast(0L));
    }

    @Test(timeout = 4000)
    public void testSaturatedCast_aboveMax_returnsMaxByte() {
        assertEquals(Byte.MAX_VALUE, SignedBytes.saturatedCast(209L));
    }

    @Test(timeout = 4000)
    public void testSaturatedCast_belowMin_returnsMinByte() {
        assertEquals(Byte.MIN_VALUE, SignedBytes.saturatedCast(-2776L));
    }

    // Tests for compare method
    @Test(timeout = 4000)
    public void testCompare_equalValues() {
        assertEquals(0, SignedBytes.compare((byte) 0, (byte) 0));
    }

    @Test(timeout = 4000)
    public void testCompare_firstLarger() {
        assertTrue(SignedBytes.compare((byte) 111, (byte) 0) > 0);
    }

    @Test(timeout = 4000)
    public void testCompare_secondLarger() {
        assertTrue(SignedBytes.compare((byte) 0, (byte) 84) < 0);
    }

    // Tests for min method
    @Test(timeout = 4000)
    public void testMin_positiveValues() {
        byte[] values = {39, 110, 54};
        assertEquals(39, SignedBytes.min(values));
    }

    @Test(timeout = 4000)
    public void testMin_negativeValue() {
        byte[] values = {0, -69};
        assertEquals(-69, SignedBytes.min(values));
    }

    @Test(timeout = 4000)
    public void testMin_zeroIsSmallest() {
        byte[] values = {16, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(0, SignedBytes.min(values));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testMin_nullArray_throwsException() {
        SignedBytes.min(null);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testMin_emptyArray_throwsException() {
        SignedBytes.min(new byte[0]);
    }

    // Tests for max method
    @Test(timeout = 4000)
    public void testMax_negativeValue() {
        byte[] values = {-81};
        assertEquals(-81, SignedBytes.max(values));
    }

    @Test(timeout = 4000)
    public void testMax_positiveValue() {
        byte[] values = {0, 110, 0};
        assertEquals(110, SignedBytes.max(values));
    }

    @Test(timeout = 4000)
    public void testMax_allZeros() {
        byte[] values = {0, 0, 0};
        assertEquals(0, SignedBytes.max(values));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testMax_nullArray_throwsException() {
        SignedBytes.max(null);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testMax_emptyArray_throwsException() {
        SignedBytes.max(new byte[0]);
    }

    // Tests for join method
    @Test(timeout = 4000)
    public void testJoin_emptyArray_returnsEmptyString() {
        assertEquals("", SignedBytes.join("1", new byte[0]));
    }

    @Test(timeout = 4000)
    public void testJoin_nonEmptyArray_returnsJoinedString() {
        byte[] values = {0, 0, 0};
        String result = SignedBytes.join("&#GMks!-I`k", values);
        assertEquals("0&#GMks!-I`k0&#GMks!-I`k0", result);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testJoin_nullArray_throwsException() {
        SignedBytes.join("Out of range: %s", null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testJoin_nullSeparator_throwsException() {
        SignedBytes.join(null, new byte[13]);
    }

    // Tests for sortDescending method
    @Test(timeout = 4000)
    public void testSortDescending_subRange() {
        byte[] values = new byte[6];
        SignedBytes.sortDescending(values, 0, 1);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0}, values);
    }

    @Test(timeout = 4000)
    public void testSortDescending_entireArray() {
        byte[] values = new byte[3];
        SignedBytes.sortDescending(values);
        assertEquals(3, values.length);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testSortDescending_nullArray_throwsException() {
        SignedBytes.sortDescending(null);
    }

    @Test(timeout = 4000, expected = IndexOutOfBoundsException.class)
    public void testSortDescending_invalidRange_throwsException() {
        SignedBytes.sortDescending(new byte[13], 127, 127);
    }

    // Test for lexicographicalComparator
    @Test(timeout = 4000)
    public void testLexicographicalComparator_exists() {
        Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
        assertNotNull(comparator);
    }
}