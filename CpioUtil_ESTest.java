package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.compress.archivers.cpio.CpioUtil;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CpioUtil_ESTest extends CpioUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testLongToByteArrayWithNegativeNumber() throws Throwable {
        // Test conversion of a negative long to a byte array of specific length
        byte[] byteArray = CpioUtil.long2byteArray(-3070L, 146, false);
        assertEquals(146, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testFileTypeWithZeroMode() throws Throwable {
        // Test fileType method with mode 0
        long fileType = CpioUtil.fileType(0L);
        assertEquals(0L, fileType);
    }

    @Test(timeout = 4000)
    public void testByteArrayToLongWithNegativeByte() throws Throwable {
        // Test conversion of a byte array with a negative byte to a long
        byte[] byteArray = new byte[8];
        byteArray[1] = (byte) (-51);
        long result = CpioUtil.byteArray2long(byteArray, false);
        assertEquals(-3674937295934324736L, result);
    }

    @Test(timeout = 4000)
    public void testLongToByteArrayWithNegativeLength() {
        // Test conversion of a long to a byte array with negative length
        try {
            CpioUtil.long2byteArray(0L, -3182, true);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            verifyException("org.apache.commons.compress.archivers.cpio.CpioUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testByteArrayToLongWithNullArray() {
        // Test conversion of a null byte array to a long
        try {
            CpioUtil.byteArray2long(null, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.cpio.CpioUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testByteArrayToLongWithEmptyArray() {
        // Test conversion of an empty byte array to a long
        byte[] byteArray = new byte[0];
        try {
            CpioUtil.byteArray2long(byteArray, false);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.compress.archivers.cpio.CpioUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testLongToByteArrayWithSmallValues() throws Throwable {
        // Test conversion of a small long value to a byte array
        byte[] byteArray = CpioUtil.long2byteArray((byte) 2, (byte) 2, false);
        assertArrayEquals(new byte[]{(byte) 2, (byte) 0}, byteArray);
    }

    @Test(timeout = 4000)
    public void testLongToByteArrayWithUnsupportedLength() {
        // Test conversion of a long to a byte array with unsupported length
        try {
            CpioUtil.long2byteArray(3061L, (byte) 0, true);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.compress.archivers.cpio.CpioUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testFileTypeWithSpecificMode() throws Throwable {
        // Test fileType method with a specific mode
        long fileType = CpioUtil.fileType(61440L);
        assertEquals(61440L, fileType);
    }

    @Test(timeout = 4000)
    public void testCpioUtilConstructor() {
        // Test instantiation of CpioUtil
        CpioUtil cpioUtil = new CpioUtil();
        assertNotNull(cpioUtil);
    }
}