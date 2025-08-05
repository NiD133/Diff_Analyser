package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import org.apache.commons.compress.harmony.unpack200.CpBands;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry;
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
public class SegmentConstantPool_ESTest extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMatchSpecificPoolEntryIndexReturnsMinusOneForInvalidIndex() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        String[] stringArray = new String[6];
        int result = segmentConstantPool.matchSpecificPoolEntryIndex(stringArray, stringArray[0], -182);
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testMatchSpecificPoolEntryIndexReturnsMinusOneForEmptyString() throws Throwable {
        String[] stringArray = new String[4];
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        int result = segmentConstantPool.matchSpecificPoolEntryIndex(stringArray, stringArray[2], 5);
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testGetValueThrowsNullPointerException() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        try {
            segmentConstantPool.getValue(9, 9);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetValueWithNegativeIndexThrowsError() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        try {
            segmentConstantPool.getValue(-282, 4294967296L);
            fail("Expecting exception: Error");
        } catch (Error e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetInitMethodPoolEntryThrowsIOException() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        try {
            segmentConstantPool.getInitMethodPoolEntry(3120, 3120, "Tried to get a value I don't know about: ");
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testRegexMatchesReturnsFalseForNonMatchingPattern() throws Throwable {
        boolean result = SegmentConstantPool.regexMatches("^<init>.*", "v_g:,");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testToIntExactReturnsZeroForZeroLong() throws Throwable {
        int result = SegmentConstantPool.toIntExact(0L);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testToIntExactReturnsNegativeValueForNegativeLong() throws Throwable {
        int result = SegmentConstantPool.toIntExact(-119L);
        assertEquals(-119, result);
    }

    @Test(timeout = 4000)
    public void testToIndexReturnsZeroForZeroLong() throws Throwable {
        int result = SegmentConstantPool.toIndex(0L);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testToIndexReturnsOneForOneLong() throws Throwable {
        int result = SegmentConstantPool.toIndex(1L);
        assertEquals(1, result);
    }

    @Test(timeout = 4000)
    public void testRegexMatchesReturnsTrueForMatchingPattern() throws Throwable {
        boolean result = SegmentConstantPool.regexMatches(".*", ".*");
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testRegexMatchesReturnsFalseForEmptyString() throws Throwable {
        boolean result = SegmentConstantPool.regexMatches("^<init>.*", "");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testMatchSpecificPoolEntryIndexReturnsZeroForValidIndex() throws Throwable {
        String[] stringArray = new String[8];
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        int result = segmentConstantPool.matchSpecificPoolEntryIndex(stringArray, stringArray[2], 0);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testToIntExactThrowsIOExceptionForLargeLong() throws Throwable {
        try {
            SegmentConstantPool.toIntExact(4294967294L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testToIndexThrowsIOExceptionForNegativeLong() throws Throwable {
        try {
            SegmentConstantPool.toIndex(-1975L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testRegexMatchesThrowsNullPointerExceptionForNullString() throws Throwable {
        try {
            SegmentConstantPool.regexMatches("^<init>.*", null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testRegexMatchesThrowsErrorForUnknownPattern() throws Throwable {
        try {
            SegmentConstantPool.regexMatches("gml`F", "6x,WAe3");
            fail("Expecting exception: Error");
        } catch (Error e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testMatchSpecificPoolEntryIndexThrowsArrayIndexOutOfBoundsException() throws Throwable {
        String[] stringArray0 = new String[0];
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        String[] stringArray1 = new String[2];
        try {
            segmentConstantPool.matchSpecificPoolEntryIndex(stringArray1, stringArray0, stringArray1[1], ":M", 2501);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testMatchSpecificPoolEntryIndexThrowsNullPointerExceptionForNullArray() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        try {
            segmentConstantPool.matchSpecificPoolEntryIndex(null, null, 5);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPoolArrayCache$CachedArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetClassPoolEntryThrowsNullPointerException() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        try {
            segmentConstantPool.getClassPoolEntry("f(`.fI");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testMatchSpecificPoolEntryIndexReturnsOneForMatchingPattern() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        String[] stringArray = new String[8];
        stringArray[1] = "^<init>.*";
        int result = segmentConstantPool.matchSpecificPoolEntryIndex(stringArray, "^<init>.*", 0);
        assertEquals(1, result);
    }

    @Test(timeout = 4000)
    public void testGetValueReturnsNullForNegativeLongIndex() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        ClassFileEntry classFileEntry = segmentConstantPool.getValue(12, -1L);
        assertNull(classFileEntry);
    }

    @Test(timeout = 4000)
    public void testGetValueThrowsIOExceptionForNegativeRange() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        try {
            segmentConstantPool.getValue(7, -460L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetInitMethodPoolEntryThrowsIOExceptionForInvalidType() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        try {
            segmentConstantPool.getInitMethodPoolEntry(8, 8, "4(");
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetConstantPoolEntryThrowsIOExceptionForUnsupportedType() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        try {
            segmentConstantPool.getConstantPoolEntry(9, 4);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetConstantPoolEntryThrowsIOExceptionForNegativeIndex() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        try {
            segmentConstantPool.getConstantPoolEntry(0, -1988L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testMatchSpecificPoolEntryIndexHandlesMultipleMatches() throws Throwable {
        String[] stringArray = new String[11];
        stringArray[2] = "^]LzTC)tW7*]J5tWdvD$";
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        int result = segmentConstantPool.matchSpecificPoolEntryIndex(stringArray, stringArray, "^]LzTC)tW7*]J5tWdvD$", "^<init>.*", -1311);
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testMatchSpecificPoolEntryIndexThrowsErrorForUnknownPattern() throws Throwable {
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        String[] stringArray = new String[1];
        stringArray[0] = "v^<iPit>.*";
        try {
            segmentConstantPool.matchSpecificPoolEntryIndex(stringArray, stringArray, "v^<iPit>.*", "v^<iPit>.*", -1089);
            fail("Expecting exception: Error");
        } catch (Error e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }

    @Test(timeout = 4000)
    public void testToIntExactReturnsOneForOneLong() throws Throwable {
        int result = SegmentConstantPool.toIntExact(1);
        assertEquals(1, result);
    }
}