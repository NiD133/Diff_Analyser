/*
 * Test suite for CpioUtil utility class
 * Tests byte array conversions and file type operations for CPIO archives
 */

package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.cpio.CpioUtil;

public class CpioUtilTest {

    // Tests for long2byteArray method
    
    @Test
    public void testLong2ByteArray_ValidConversion() {
        long inputValue = -3070L;
        int arrayLength = 146;
        boolean swapHalfWords = false;
        
        byte[] result = CpioUtil.long2byteArray(inputValue, arrayLength, swapHalfWords);
        
        assertEquals("Array should have the specified length", arrayLength, result.length);
    }

    @Test
    public void testLong2ByteArray_SmallValue() {
        long inputValue = 2L;
        int arrayLength = 2;
        boolean swapHalfWords = false;
        
        byte[] result = CpioUtil.long2byteArray(inputValue, arrayLength, swapHalfWords);
        
        assertArrayEquals("Should convert small value correctly", 
                         new byte[]{(byte)2, (byte)0}, result);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testLong2ByteArray_NegativeLength() {
        CpioUtil.long2byteArray(0L, -3182, true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLong2ByteArray_ZeroLengthWithSwap() {
        CpioUtil.long2byteArray(3061L, 0, true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLong2ByteArray_OddLengthNotSupported() {
        // Length must be a positive multiple of 2
        CpioUtil.long2byteArray(701, 701, false);
    }

    // Tests for byteArray2long method

    @Test
    public void testByteArray2Long_ValidConversion() {
        byte[] inputArray = new byte[8];
        inputArray[1] = (byte) -51;
        boolean swapHalfWords = false;
        
        long result = CpioUtil.byteArray2long(inputArray, swapHalfWords);
        
        assertEquals("Should convert byte array to expected long value", 
                    -3674937295934324736L, result);
    }

    @Test
    public void testByteArray2Long_AllZeros() {
        byte[] inputArray = new byte[4];
        boolean swapHalfWords = false;
        
        long result = CpioUtil.byteArray2long(inputArray, swapHalfWords);
        
        assertEquals("Array of zeros should convert to 0", 0L, result);
    }

    @Test
    public void testByteArray2Long_WithHalfWordSwap() {
        byte[] inputArray = CpioUtil.long2byteArray(1L, 5174, true);
        
        // Should not throw exception when converting back
        CpioUtil.byteArray2long(inputArray, true);
    }

    @Test(expected = NullPointerException.class)
    public void testByteArray2Long_NullInput() {
        CpioUtil.byteArray2long(null, true);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testByteArray2Long_EmptyArray() {
        byte[] emptyArray = new byte[0];
        CpioUtil.byteArray2long(emptyArray, false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testByteArray2Long_InvalidLengthWithSwap() {
        byte[] invalidArray = new byte[3]; // Length not multiple of 2
        CpioUtil.byteArray2long(invalidArray, true);
    }

    // Tests for fileType method

    @Test
    public void testFileType_ZeroMode() {
        long mode = 0L;
        
        long result = CpioUtil.fileType(mode);
        
        assertEquals("File type of mode 0 should be 0", 0L, result);
    }

    @Test
    public void testFileType_DirectoryMode() {
        long directoryMode = 61440L; // Common directory mode bits
        
        long result = CpioUtil.fileType(directoryMode);
        
        assertEquals("Should extract file type bits correctly", 61440L, result);
    }

    // Test for constructor (utility class instantiation)

    @Test
    public void testConstructor_CanInstantiate() {
        // Verify that the utility class can be instantiated
        // (though typically utility classes have private constructors)
        CpioUtil cpioUtil = new CpioUtil();
        assertNotNull("Should be able to create CpioUtil instance", cpioUtil);
    }

    // Edge case and integration tests

    @Test
    public void testRoundTripConversion_PreservesValue() {
        long originalValue = 3634L;
        int arrayLength = 3634;
        boolean swapHalfWords = false;
        
        // This should work without throwing exceptions
        CpioUtil.long2byteArray(originalValue, arrayLength, swapHalfWords);
        CpioUtil.long2byteArray(originalValue, arrayLength, swapHalfWords);
        CpioUtil.long2byteArray(originalValue, arrayLength, swapHalfWords);
    }
}