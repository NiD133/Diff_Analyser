package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.io.ByteOrderMark;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class ByteOrderMark_ESTest extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMatches_UTF16BE_WithIncorrectFirstByte_ReturnsFalse() throws Throwable {
        ByteOrderMark bom = ByteOrderMark.UTF_16BE;
        int[] testBytes = new int[8];
        testBytes[0] = '\uFEFF'; // Incorrect first byte for UTF-16BE
        assertFalse("UTF-16BE should not match array with incorrect first byte", bom.matches(testBytes));
    }

    @Test(timeout = 4000)
    public void testHashCode_UTF8_DoesNotThrowException() throws Throwable {
        ByteOrderMark.UTF_8.hashCode();
    }

    @Test(timeout = 4000)
    public void testEquals_UTF16BEAndUTF16LE_ReturnsFalse() throws Throwable {
        ByteOrderMark bom1 = ByteOrderMark.UTF_16BE;
        ByteOrderMark bom2 = ByteOrderMark.UTF_16LE;
        assertNotEquals("UTF-16BE should not equal UTF-16LE", bom1, bom2);
    }

    @Test(timeout = 4000)
    public void testEquals_UTF8AndUTF16BE_ReturnsFalse() throws Throwable {
        ByteOrderMark bom1 = ByteOrderMark.UTF_8;
        ByteOrderMark bom2 = ByteOrderMark.UTF_16BE;
        assertNotEquals("UTF-8 should not equal UTF-16BE", bom1, bom2);
    }

    @Test(timeout = 4000)
    public void testGet_UTF32BE_Index3_Returns255() throws Throwable {
        ByteOrderMark bom = ByteOrderMark.UTF_32BE;
        assertEquals("Third byte of UTF-32BE should be 255", 255, bom.get(3));
    }

    @Test(timeout = 4000)
    public void testGet_CustomBOM_Index2_ReturnsNegative127() throws Throwable {
        int[] bytes = {0, 0, -127, 0};
        ByteOrderMark bom = new ByteOrderMark("@P", bytes);
        assertEquals("Byte at index 2 should be -127", -127, bom.get(2));
    }

    @Test(timeout = 4000)
    public void testGet_UTF8_NegativeIndex_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        ByteOrderMark bom = ByteOrderMark.UTF_8;
        try {
            bom.get(-5);
            fail("Expected ArrayIndexOutOfBoundsException for negative index");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Exception message should contain the index", "-5", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_NullBytes_ThrowsNullPointerException() throws Throwable {
        try {
            new ByteOrderMark("U", (int[]) null);
            fail("Expected NullPointerException for null bytes");
        } catch (NullPointerException e) {
            assertTrue("Exception should mention 'bytes'", e.getMessage().contains("bytes"));
        }
    }

    @Test(timeout = 4000)
    public void testLength_UTF8_Returns3() throws Throwable {
        ByteOrderMark bom = ByteOrderMark.UTF_8;
        assertEquals("UTF-8 BOM length should be 3", 3, bom.length());
    }

    @Test(timeout = 4000)
    public void testToString_CustomBOM_ReturnsExpectedString() throws Throwable {
        int[] bytes = new int[6];
        ByteOrderMark bom = new ByteOrderMark("Bt^D2-fqe2We[-^J#", bytes);
        String expected = "ByteOrderMark[Bt^D2-fqe2We[-^J#: 0x0,0x0,0x0,0x0,0x0,0x0]";
        assertEquals("toString should return expected representation", expected, bom.toString());
    }

    @Test(timeout = 4000)
    public void testMatches_UTF16BE_WithZeroArray_ReturnsFalse() throws Throwable {
        ByteOrderMark bom = ByteOrderMark.UTF_16BE;
        assertFalse("UTF-16BE should not match all zeros", bom.matches(new int[8]));
    }

    @Test(timeout = 4000)
    public void testMatches_UTF8_WithTooShortArray_ReturnsFalse() throws Throwable {
        ByteOrderMark bom = ByteOrderMark.UTF_8;
        assertFalse("UTF-8 should not match array shorter than BOM", bom.matches(new int[1]));
    }

    @Test(timeout = 4000)
    public void testMatches_UTF32BE_WithNullArray_ReturnsFalse() throws Throwable {
        ByteOrderMark bom = ByteOrderMark.UTF_32BE;
        assertFalse("Should return false for null array", bom.matches(null));
    }

    @Test(timeout = 4000)
    public void testMatches_CustomBOM_WithRawBytes_ReturnsTrue() throws Throwable {
        int[] bytes = {0};
        ByteOrderMark bom = new ByteOrderMark("eS5", bytes);
        assertTrue("BOM should match its own raw bytes", bom.matches(bom.getRawBytes()));
    }

    @Test(timeout = 4000)
    public void testMatches_CustomBOM_WithSameArray_ReturnsTrue() throws Throwable {
        int[] bytes = new int[6];
        ByteOrderMark bom = new ByteOrderMark("Bt^D2-fqe2We[-^J#", bytes);
        assertTrue("BOM should match identical array", bom.matches(bytes));
    }

    @Test(timeout = 4000)
    public void testGetBytes_UTF32LE_ReturnsExpected() throws Throwable {
        ByteOrderMark bom = ByteOrderMark.UTF_32LE;
        byte[] expected = {(byte) 0xFF, (byte) 0xFE, 0x00, 0x00};
        assertArrayEquals("UTF-32LE bytes should match expected", expected, bom.getBytes());
    }

    @Test(timeout = 4000)
    public void testEquals_UTF32LEAndUTF32BE_ReturnsFalse() throws Throwable {
        ByteOrderMark bom1 = ByteOrderMark.UTF_32LE;
        ByteOrderMark bom2 = ByteOrderMark.UTF_32BE;
        assertNotEquals("UTF-32LE should not equal UTF-32BE", bom1, bom2);
    }

    @Test(timeout = 4000)
    public void testEquals_UTF32LEAndCustomBOM_ReturnsFalse() throws Throwable {
        ByteOrderMark bom1 = ByteOrderMark.UTF_32LE;
        ByteOrderMark bom2 = new ByteOrderMark("str", new int[7]);
        assertNotEquals("UTF-32LE should not equal custom BOM", bom1, bom2);
    }

    @Test(timeout = 4000)
    public void testEquals_CustomBOMAndObject_ReturnsFalse() throws Throwable {
        int[] bytes = new int[6];
        ByteOrderMark bom = new ByteOrderMark("Bt^D2-fqe2We[-^J#", bytes);
        assertFalse("BOM should not equal arbitrary object", bom.equals(new Object()));
    }

    @Test(timeout = 4000)
    public void testConstructor_EmptyBytes_ThrowsIllegalArgumentException() throws Throwable {
        try {
            new ByteOrderMark("+5{ll_Ze`iam[i)|fP", new int[0]);
            fail("Expected IllegalArgumentException for empty bytes");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention no bytes", e.getMessage().contains("No bytes specified"));
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_EmptyCharsetName_ThrowsIllegalArgumentException() throws Throwable {
        try {
            new ByteOrderMark("", new int[3]);
            fail("Expected IllegalArgumentException for empty charset name");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention no charsetName", e.getMessage().contains("No charsetName specified"));
        }
    }

    @Test(timeout = 4000)
    public void testGetCharsetName_CustomBOM_ReturnsExpected() throws Throwable {
        int[] bytes = new int[6];
        ByteOrderMark bom = new ByteOrderMark("Bt^D2-fqe2We[-^J#", bytes);
        assertEquals("Charset name should match", "Bt^D2-fqe2We[-^J#", bom.getCharsetName());
    }

    @Test(timeout = 4000)
    public void testEquals_SameInstance_ReturnsTrue() throws Throwable {
        ByteOrderMark bom = ByteOrderMark.UTF_32LE;
        assertTrue("BOM should equal itself", bom.equals(bom));
    }

    @Test(timeout = 4000)
    public void testGet_CustomBOM_Index3_Returns0() throws Throwable {
        int[] bytes = new int[8];
        ByteOrderMark bom = new ByteOrderMark("U", bytes);
        assertEquals("Byte at index 3 should be 0", 0, bom.get(3));
    }
}