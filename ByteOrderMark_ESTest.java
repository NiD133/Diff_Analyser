package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ByteOrderMarkTest extends ByteOrderMarkTestScaffolding {

    @Test(timeout = 4000)
    public void testUTF16BEMatchWithDifferentArray() {
        ByteOrderMark bom = ByteOrderMark.UTF_16BE;
        int[] testArray = new int[8];
        testArray[0] = (int) ByteOrderMark.UTF_BOM;
        assertFalse(bom.matches(testArray));
    }

    @Test(timeout = 4000)
    public void testUTF8HashCode() {
        ByteOrderMark bom = ByteOrderMark.UTF_8;
        int hashCode = bom.hashCode();
        assertNotNull(hashCode);
    }

    @Test(timeout = 4000)
    public void testUTF16LEAndUTF16BENotEqual() {
        ByteOrderMark bomLE = ByteOrderMark.UTF_16LE;
        ByteOrderMark bomBE = ByteOrderMark.UTF_16BE;
        assertFalse(bomLE.equals(bomBE));
        assertFalse(bomBE.equals(bomLE));
    }

    @Test(timeout = 4000)
    public void testUTF8AndUTF16BENotEqual() {
        ByteOrderMark bomUTF8 = ByteOrderMark.UTF_8;
        ByteOrderMark bomUTF16BE = ByteOrderMark.UTF_16BE;
        assertFalse(bomUTF8.equals(bomUTF16BE));
    }

    @Test(timeout = 4000)
    public void testUTF32BEGetByte() {
        ByteOrderMark bom = ByteOrderMark.UTF_32BE;
        assertEquals(255, bom.get(3));
    }

    @Test(timeout = 4000)
    public void testCustomBOMGetByte() {
        int[] bytes = new int[]{0, 0, -127, 0};
        ByteOrderMark bom = new ByteOrderMark("CustomBOM", bytes);
        assertEquals(-127, bom.get(2));
    }

    @Test(timeout = 4000)
    public void testGetWithInvalidIndexThrowsException() {
        ByteOrderMark bom = ByteOrderMark.UTF_8;
        try {
            bom.get(-5);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("-5", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testNullBytesThrowsException() {
        try {
            new ByteOrderMark("U", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("bytes", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testUTF8Length() {
        ByteOrderMark bom = ByteOrderMark.UTF_8;
        assertEquals(3, bom.length());
    }

    @Test(timeout = 4000)
    public void testCustomBOMToString() {
        int[] bytes = new int[6];
        ByteOrderMark bom = new ByteOrderMark("CustomBOM", bytes);
        assertEquals("ByteOrderMark[CustomBOM: 0x0,0x0,0x0,0x0,0x0,0x0]", bom.toString());
    }

    @Test(timeout = 4000)
    public void testUTF16BEMatchWithEmptyArray() {
        ByteOrderMark bom = ByteOrderMark.UTF_16BE;
        int[] testArray = new int[8];
        assertFalse(bom.matches(testArray));
    }

    @Test(timeout = 4000)
    public void testUTF8MatchWithSingleElementArray() {
        ByteOrderMark bom = ByteOrderMark.UTF_8;
        int[] testArray = new int[1];
        assertFalse(bom.matches(testArray));
    }

    @Test(timeout = 4000)
    public void testUTF32BEMatchWithNullArray() {
        ByteOrderMark bom = ByteOrderMark.UTF_32BE;
        assertFalse(bom.matches(null));
    }

    @Test(timeout = 4000)
    public void testCustomBOMMatchesRawBytes() {
        int[] bytes = new int[1];
        ByteOrderMark bom = new ByteOrderMark("CustomBOM", bytes);
        assertTrue(bom.matches(bom.getRawBytes()));
    }

    @Test(timeout = 4000)
    public void testCustomBOMMatchesItself() {
        int[] bytes = new int[6];
        ByteOrderMark bom = new ByteOrderMark("CustomBOM", bytes);
        assertTrue(bom.matches(bytes));
    }

    @Test(timeout = 4000)
    public void testUTF32LEGetBytes() {
        ByteOrderMark bom = ByteOrderMark.UTF_32LE;
        assertArrayEquals(new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0}, bom.getBytes());
    }

    @Test(timeout = 4000)
    public void testUTF32LEAndUTF32BENotEqual() {
        ByteOrderMark bomLE = ByteOrderMark.UTF_32LE;
        ByteOrderMark bomBE = ByteOrderMark.UTF_32BE;
        assertFalse(bomLE.equals(bomBE));
        assertFalse(bomBE.equals(bomLE));
    }

    @Test(timeout = 4000)
    public void testUTF32LEAndCustomBOMNotEqual() {
        ByteOrderMark bomLE = ByteOrderMark.UTF_32LE;
        int[] bytes = new int[7];
        ByteOrderMark customBOM = new ByteOrderMark("CustomBOM", bytes);
        assertFalse(bomLE.equals(customBOM));
    }

    @Test(timeout = 4000)
    public void testCustomBOMNotEqualToObject() {
        int[] bytes = new int[6];
        ByteOrderMark bom = new ByteOrderMark("CustomBOM", bytes);
        assertFalse(bom.equals(new Object()));
    }

    @Test(timeout = 4000)
    public void testEmptyBytesThrowsException() {
        int[] bytes = new int[0];
        try {
            new ByteOrderMark("InvalidBOM", bytes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("No bytes specified", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testEmptyCharsetNameThrowsException() {
        int[] bytes = new int[3];
        try {
            new ByteOrderMark("", bytes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("No charsetName specified", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCustomBOMGetCharsetName() {
        int[] bytes = new int[6];
        ByteOrderMark bom = new ByteOrderMark("CustomBOM", bytes);
        assertEquals("CustomBOM", bom.getCharsetName());
    }

    @Test(timeout = 4000)
    public void testUTF32LEEqualsItself() {
        ByteOrderMark bom = ByteOrderMark.UTF_32LE;
        assertTrue(bom.equals(bom));
    }

    @Test(timeout = 4000)
    public void testCustomBOMGetByteAtPosition() {
        int[] bytes = new int[8];
        ByteOrderMark bom = new ByteOrderMark("CustomBOM", bytes);
        assertEquals(0, bom.get(3));
    }
}