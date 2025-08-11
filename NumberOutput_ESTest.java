package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.NumberOutput;
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
public class NumberOutput_ESTest extends NumberOutput_ESTest_scaffolding {

    private static final long LARGE_LONG_VALUE = 9164449253911987585L;
    private static final int LARGE_INT_VALUE = 2084322301;
    private static final int SMALL_BUFFER_SIZE = 3;
    private static final int NEGATIVE_OFFSET = -48;

    @Test(timeout = 4000)
    public void testOutputLongWithNullByteArrayThrowsNullPointerException() {
        try {
            NumberOutput.outputLong(99L, null, 91);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }

    @Test(timeout = 4000)
    public void testOutputLongWithSmallByteArrayThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[SMALL_BUFFER_SIZE];
        try {
            NumberOutput.outputLong(LARGE_LONG_VALUE, byteArray, NEGATIVE_OFFSET);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }

    @Test(timeout = 4000)
    public void testOutputLongWithNullCharArrayThrowsNullPointerException() {
        try {
            NumberOutput.outputLong(99358116390671185L, null, -2869);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }

    @Test(timeout = 4000)
    public void testOutputLongWithNegativeOffsetThrowsArrayIndexOutOfBoundsException() {
        char[] charArray = new char[8];
        try {
            NumberOutput.outputLong(9007199254740992000L, charArray, -2118);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }

    @Test(timeout = 4000)
    public void testToStringWithNegativeLongValue() {
        String result = NumberOutput.toString(-2147483648L);
        assertEquals("-2147483648", result);
    }

    @Test(timeout = 4000)
    public void testToStringWithPositiveLongValue() {
        String result = NumberOutput.toString(2147483647L);
        assertEquals("2147483647", result);
    }

    @Test(timeout = 4000)
    public void testToStringWithNegativeByteValue() {
        String result = NumberOutput.toString((long) (byte) -11);
        assertEquals("-11", result);
    }

    @Test(timeout = 4000)
    public void testToStringWithSmallIntValue() {
        String result = NumberOutput.toString(1);
        assertEquals("1", result);
    }

    @Test(timeout = 4000)
    public void testOutputLongWithSmallBufferThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[1];
        try {
            NumberOutput.outputLong(2147483647L, byteArray, 0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }

    @Test(timeout = 4000)
    public void testOutputIntWithNullByteArrayThrowsNullPointerException() {
        try {
            NumberOutput.outputInt(10, null, 199);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }

    @Test(timeout = 4000)
    public void testOutputIntWithLargeOffsetThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[16];
        try {
            NumberOutput.outputInt(LARGE_INT_VALUE, byteArray, LARGE_INT_VALUE);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.fasterxml.jackson.core.io.NumberOutput", e);
        }
    }

    // Additional tests can be added here following the same pattern

}