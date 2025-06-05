package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.io.ByteOrderMark;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true,
    useJEE = true
)
public class ByteOrderMark_ESTest extends ByteOrderMark_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testUTF16BEMatchWithDifferentArray() {
        ByteOrderMark utf16be = ByteOrderMark.UTF_16BE;
        int[] differentArray = {(int) '\uFEFF', 0};
        boolean matches = utf16be.matches(differentArray);
        assertFalse("UTF-16BE should not match a different array", matches);
    }

    @Test(timeout = 4000)
    public void testUTF16BEAndUTF16LENotEqual() {
        ByteOrderMark utf16be = ByteOrderMark.UTF_16BE;
        ByteOrderMark utf16le = ByteOrderMark.UTF_16LE;
        assertFalse("UTF-16BE should not equal UTF-16LE", utf16be.equals(utf16le));
        assertFalse("UTF-16LE should not equal UTF-16BE", utf16le.equals(utf16be));
    }

    @Test(timeout = 4000)
    public void testUTF16LEAndUTF32LENotEqual() {
        ByteOrderMark utf16le = ByteOrderMark.UTF_16LE;
        ByteOrderMark utf32le = ByteOrderMark.UTF_32LE;
        assertFalse("UTF-16LE should not equal UTF-32LE", utf16le.equals(utf32le));
    }

    @Test(timeout = 4000)
    public void testUTF16LEEqualsItself() {
        ByteOrderMark utf16le = ByteOrderMark.UTF_16LE;
        assertTrue("UTF-16LE should equal itself", utf16le.equals(utf16le));
    }

    @Test(timeout = 4000)
    public void testUTF32LEGetThirdByte() {
        ByteOrderMark utf32le = ByteOrderMark.UTF_32LE;
        int thirdByte = utf32le.get(2);
        assertEquals("The third byte of UTF-32LE should be 0", 0, thirdByte);
    }

    @Test(timeout = 4000)
    public void testUTF32LEGetSecondByte() {
        ByteOrderMark utf32le = ByteOrderMark.UTF_32LE;
        int secondByte = utf32le.get(1);
        assertEquals("The second byte of UTF-32LE should be 254", 254, secondByte);
    }

    @Test(timeout = 4000)
    public void testCustomByteOrderMarkNegativeByte() {
        int[] customBytes = {-8, 0, 0, 0};
        ByteOrderMark customBOM = new ByteOrderMark("+U", customBytes);
        int firstByte = customBOM.get(0);
        assertEquals("The first byte of custom BOM should be -8", -8, firstByte);
    }

    @Test(timeout = 4000)
    public void testNullBytesThrowsException() {
        try {
            new ByteOrderMark("pLt'", null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testUTF16BELength() {
        ByteOrderMark utf16be = ByteOrderMark.UTF_16BE;
        int length = utf16be.length();
        assertEquals("UTF-16BE should have length 2", 2, length);
    }

    @Test(timeout = 4000)
    public void testUTF16BEGetOutOfBounds() {
        ByteOrderMark utf16be = ByteOrderMark.UTF_16BE;
        try {
            utf16be.get(2);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.ByteOrderMark", e);
        }
    }

    @Test(timeout = 4000)
    public void testUTF8ToString() {
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        String bomString = utf8.toString();
        assertEquals("UTF-8 BOM string representation mismatch", "ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]", bomString);
    }

    @Test(timeout = 4000)
    public void testUTF16LEMatchWithDifferentArray() {
        ByteOrderMark utf16le = ByteOrderMark.UTF_16LE;
        int[] differentArray = {0, 0, 0};
        boolean matches = utf16le.matches(differentArray);
        assertFalse("UTF-16LE should not match a different array", matches);
    }

    @Test(timeout = 4000)
    public void testUTF8MatchWithShortArray() {
        int[] shortArray = {0};
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        boolean matches = utf8.matches(shortArray);
        assertFalse("UTF-8 should not match a short array", matches);
    }

    @Test(timeout = 4000)
    public void testUTF16BEMatchWithNull() {
        ByteOrderMark utf16be = ByteOrderMark.UTF_16BE;
        boolean matches = utf16be.matches(null);
        assertFalse("UTF-16BE should not match null", matches);
    }

    @Test(timeout = 4000)
    public void testUTF8MatchWithRawBytes() {
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        int[] rawBytes = utf8.getRawBytes();
        boolean matches = utf8.matches(rawBytes);
        assertTrue("UTF-8 should match its raw bytes", matches);
    }

    @Test(timeout = 4000)
    public void testUTF8HashCode() {
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        utf8.hashCode(); // Just to ensure no exceptions are thrown
    }

    @Test(timeout = 4000)
    public void testUTF8GetBytes() {
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        byte[] bytes = utf8.getBytes();
        assertArrayEquals("UTF-8 byte array mismatch", new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}, bytes);
    }

    @Test(timeout = 4000)
    public void testUTF16LEAndUTF16BENotEqual() {
        ByteOrderMark utf16le = ByteOrderMark.UTF_16LE;
        ByteOrderMark utf16be = ByteOrderMark.UTF_16BE;
        assertFalse("UTF-16LE should not equal UTF-16BE", utf16le.equals(utf16be));
        assertFalse("UTF-16BE should not equal UTF-16LE", utf16be.equals(utf16le));
    }

    @Test(timeout = 4000)
    public void testUTF32BEAndUTF16BENotEqual() {
        ByteOrderMark utf32be = ByteOrderMark.UTF_32BE;
        ByteOrderMark utf16be = ByteOrderMark.UTF_16BE;
        assertFalse("UTF-32BE should not equal UTF-16BE", utf32be.equals(utf16be));
    }

    @Test(timeout = 4000)
    public void testUTF8NotEqualToString() {
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        assertFalse("UTF-8 should not equal a string representation", utf8.equals("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]"));
    }

    @Test(timeout = 4000)
    public void testEmptyBytesThrowsException() {
        int[] emptyArray = {};
        try {
            new ByteOrderMark("N%W{9DrL", emptyArray);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.ByteOrderMark", e);
        }
    }

    @Test(timeout = 4000)
    public void testEmptyCharsetNameThrowsException() {
        int[] someArray = {0, 0, 0, 0, 0, 0, 0};
        try {
            new ByteOrderMark("", someArray);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.ByteOrderMark", e);
        }
    }

    @Test(timeout = 4000)
    public void testCustomBOMMatchesRawBytes() {
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        int[] rawBytes = utf8.getRawBytes();
        ByteOrderMark customBOM = new ByteOrderMark("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]", rawBytes);
        assertTrue("Custom BOM should match its raw bytes", customBOM.matches(rawBytes));
    }

    @Test(timeout = 4000)
    public void testUTF16BECharsetName() {
        ByteOrderMark utf16be = ByteOrderMark.UTF_16BE;
        String charsetName = utf16be.getCharsetName();
        assertEquals("UTF-16BE charset name mismatch", "UTF-16BE", charsetName);
    }
}