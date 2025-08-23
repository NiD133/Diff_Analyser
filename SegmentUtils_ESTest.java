package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class SegmentUtils_ESTest extends SegmentUtils_ESTest_scaffolding {

    private static final String EXCEPTION_MESSAGE = "Expecting exception: ";
    private static final String NO_ARGUMENTS_MESSAGE = "No arguments";
    private static final String NULL_POINTER_EXCEPTION = "NullPointerException";
    private static final String ILLEGAL_ARGUMENT_EXCEPTION = "IllegalArgumentException";

    @Test(timeout = 4000)
    public void testCountMatchesWithNullMatcherThrowsNullPointerException() {
        long[][] longArray = new long[4][7];
        longArray[0] = new long[0];
        
        try {
            SegmentUtils.countMatches(longArray, null);
            fail(EXCEPTION_MESSAGE + NULL_POINTER_EXCEPTION);
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testCountArgsWithValidString() {
        int result = SegmentUtils.countArgs("Can't read beyond end of stream (n = %,d, count = %,d, maxLength = %,d, remaining  %,d)", 103);
        assertEquals(26, result);
    }

    @Test(timeout = 4000)
    public void testCountInvokeInterfaceArgsWithNoArgs() {
        int result = SegmentUtils.countInvokeInterfaceArgs("1.8()JbKnQPeYyNxq!");
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testCountArgsWithNoArgs() {
        int result = SegmentUtils.countArgs("1.8()JbKnQPeYyNxq!", 0);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testCountArgsWithNullStringThrowsNullPointerException() {
        try {
            SegmentUtils.countArgs((String) null, 1);
            fail(EXCEPTION_MESSAGE + NULL_POINTER_EXCEPTION);
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testCountArgsWithInvalidStringThrowsIllegalArgumentException() {
        try {
            SegmentUtils.countArgs("tp!Hgy");
            fail(EXCEPTION_MESSAGE + ILLEGAL_ARGUMENT_EXCEPTION);
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testCountBit16WithEmptyLongArray() {
        long[] longArray = new long[0];
        int result = SegmentUtils.countBit16(longArray);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testCountBit16WithNegativeValue() {
        int[] intArray = new int[1];
        intArray[0] = -142;
        int result = SegmentUtils.countBit16(intArray);
        assertEquals(1, result);
    }

    @Test(timeout = 4000)
    public void testCountInvokeInterfaceArgsWithInvalidStringThrowsIllegalArgumentException() {
        try {
            SegmentUtils.countInvokeInterfaceArgs("Aw2<'N6_{7~h_K?(gZ");
            fail(EXCEPTION_MESSAGE + ILLEGAL_ARGUMENT_EXCEPTION);
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testSegmentUtilsConstructor() {
        new SegmentUtils();
    }
}