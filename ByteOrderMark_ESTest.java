package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.io.ByteOrderMark;

/**
 * Test suite for ByteOrderMark class functionality.
 * Tests BOM creation, matching, equality, and edge cases.
 */
public class ByteOrderMarkTest {

    // ========== Constructor Tests ==========
    
    @Test
    public void testConstructor_WithValidParameters_ShouldCreateBOM() {
        // Given
        String charsetName = "UTF-8";
        int[] bomBytes = {0xEF, 0xBB, 0xBF};
        
        // When
        ByteOrderMark bom = new ByteOrderMark(charsetName, bomBytes);
        
        // Then
        assertEquals(charsetName, bom.getCharsetName());
        assertEquals(3, bom.length());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithEmptyCharsetName_ShouldThrowException() {
        // Given
        String emptyCharsetName = "";
        int[] bomBytes = {0xEF, 0xBB, 0xBF};
        
        // When & Then
        new ByteOrderMark(emptyCharsetName, bomBytes);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithEmptyByteArray_ShouldThrowException() {
        // Given
        String charsetName = "UTF-8";
        int[] emptyBytes = {};
        
        // When & Then
        new ByteOrderMark(charsetName, emptyBytes);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_WithNullByteArray_ShouldThrowException() {
        // Given
        String charsetName = "UTF-8";
        int[] nullBytes = null;
        
        // When & Then
        new ByteOrderMark(charsetName, nullBytes);
    }

    // ========== Byte Access Tests ==========
    
    @Test
    public void testGet_WithValidIndex_ShouldReturnCorrectByte() {
        // Given
        ByteOrderMark utf32BE = ByteOrderMark.UTF_32BE;
        
        // When
        int lastByte = utf32BE.get(3);
        
        // Then
        assertEquals(255, lastByte); // 0xFF = 255
    }
    
    @Test
    public void testGet_WithCustomBOM_ShouldReturnCorrectByte() {
        // Given
        int[] customBytes = {0, 0, -127, 0};
        ByteOrderMark customBOM = new ByteOrderMark("CUSTOM", customBytes);
        
        // When
        int retrievedByte = customBOM.get(2);
        
        // Then
        assertEquals(-127, retrievedByte);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGet_WithNegativeIndex_ShouldThrowException() {
        // Given
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        
        // When & Then
        utf8.get(-5);
    }
    
    @Test
    public void testGetBytes_ShouldReturnCorrectByteArray() {
        // Given
        ByteOrderMark utf32LE = ByteOrderMark.UTF_32LE;
        
        // When
        byte[] bytes = utf32LE.getBytes();
        
        // Then
        byte[] expected = {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};
        assertArrayEquals(expected, bytes);
    }

    // ========== Matching Tests ==========
    
    @Test
    public void testMatches_WithExactMatch_ShouldReturnTrue() {
        // Given
        int[] testBytes = {0};
        ByteOrderMark customBOM = new ByteOrderMark("TEST", testBytes);
        
        // When
        boolean matches = customBOM.matches(testBytes);
        
        // Then
        assertTrue("BOM should match its own byte pattern", matches);
    }
    
    @Test
    public void testMatches_WithLongerArray_ShouldReturnTrue() {
        // Given
        ByteOrderMark customBOM = new ByteOrderMark("TEST", new int[]{0, 0, 0, 0, 0, 0});
        int[] longerArray = {0, 0, 0, 0, 0, 0, 1, 2, 3}; // Extra bytes at end
        
        // When
        boolean matches = customBOM.matches(longerArray);
        
        // Then
        assertTrue("BOM should match when array starts with BOM bytes", matches);
    }
    
    @Test
    public void testMatches_WithDifferentBytes_ShouldReturnFalse() {
        // Given
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;
        int[] differentBytes = {0, 0, 0, 0, 0, 0, 0, 0};
        
        // When
        boolean matches = utf16BE.matches(differentBytes);
        
        // Then
        assertFalse("BOM should not match different byte pattern", matches);
    }
    
    @Test
    public void testMatches_WithShorterArray_ShouldReturnFalse() {
        // Given
        ByteOrderMark utf8 = ByteOrderMark.UTF_8; // 3 bytes
        int[] shorterArray = {0xEF}; // Only 1 byte
        
        // When
        boolean matches = utf8.matches(shorterArray);
        
        // Then
        assertFalse("BOM should not match shorter array", matches);
    }
    
    @Test
    public void testMatches_WithNullArray_ShouldReturnFalse() {
        // Given
        ByteOrderMark utf32BE = ByteOrderMark.UTF_32BE;
        
        // When
        boolean matches = utf32BE.matches(null);
        
        // Then
        assertFalse("BOM should not match null array", matches);
    }
    
    @Test
    public void testMatches_WithPartialMatch_ShouldReturnFalse() {
        // Given
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;
        int[] partialMatch = {0xFEFF, 0, 0, 0, 0, 0, 0, 0}; // Wrong format
        
        // When
        boolean matches = utf16BE.matches(partialMatch);
        
        // Then
        assertFalse("BOM should not match partial/incorrect pattern", matches);
    }

    // ========== Equality Tests ==========
    
    @Test
    public void testEquals_WithSameInstance_ShouldReturnTrue() {
        // Given
        ByteOrderMark utf32LE = ByteOrderMark.UTF_32LE;
        
        // When
        boolean isEqual = utf32LE.equals(utf32LE);
        
        // Then
        assertTrue("BOM should equal itself", isEqual);
    }
    
    @Test
    public void testEquals_WithDifferentBOMTypes_ShouldReturnFalse() {
        // Given
        ByteOrderMark utf16LE = ByteOrderMark.UTF_16LE;
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;
        
        // When
        boolean isEqual = utf16BE.equals(utf16LE);
        
        // Then
        assertFalse("Different BOM types should not be equal", isEqual);
    }
    
    @Test
    public void testEquals_WithUTF8AndUTF16_ShouldReturnFalse() {
        // Given
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;
        
        // When
        boolean isEqual = utf8.equals(utf16BE);
        
        // Then
        assertFalse("UTF-8 and UTF-16BE BOMs should not be equal", isEqual);
    }
    
    @Test
    public void testEquals_WithUTF32Variants_ShouldReturnFalse() {
        // Given
        ByteOrderMark utf32LE = ByteOrderMark.UTF_32LE;
        ByteOrderMark utf32BE = ByteOrderMark.UTF_32BE;
        
        // When
        boolean isEqual = utf32LE.equals(utf32BE);
        
        // Then
        assertFalse("UTF-32LE and UTF-32BE should not be equal", isEqual);
    }
    
    @Test
    public void testEquals_WithCustomBOMAndStandardBOM_ShouldReturnFalse() {
        // Given
        ByteOrderMark utf32LE = ByteOrderMark.UTF_32LE;
        ByteOrderMark customBOM = new ByteOrderMark("CUSTOM", new int[]{1, 2, 3, 4, 5, 6, 7});
        
        // When
        boolean isEqual = utf32LE.equals(customBOM);
        
        // Then
        assertFalse("Standard BOM should not equal custom BOM", isEqual);
    }
    
    @Test
    public void testEquals_WithNonBOMObject_ShouldReturnFalse() {
        // Given
        ByteOrderMark customBOM = new ByteOrderMark("TEST", new int[]{1, 2, 3, 4, 5, 6});
        Object nonBOMObject = new Object();
        
        // When
        boolean isEqual = customBOM.equals(nonBOMObject);
        
        // Then
        assertFalse("BOM should not equal non-BOM object", isEqual);
    }

    // ========== Utility Method Tests ==========
    
    @Test
    public void testLength_WithUTF8_ShouldReturn3() {
        // Given
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        
        // When
        int length = utf8.length();
        
        // Then
        assertEquals("UTF-8 BOM should have length 3", 3, length);
    }
    
    @Test
    public void testGetCharsetName_WithCustomBOM_ShouldReturnCorrectName() {
        // Given
        String expectedName = "MY_CUSTOM_CHARSET";
        ByteOrderMark customBOM = new ByteOrderMark(expectedName, new int[]{1, 2, 3, 4, 5, 6});
        
        // When
        String actualName = customBOM.getCharsetName();
        
        // Then
        assertEquals("Should return correct charset name", expectedName, actualName);
    }
    
    @Test
    public void testToString_WithCustomBOM_ShouldContainNameAndBytes() {
        // Given
        String charsetName = "TEST_CHARSET";
        int[] bytes = {0, 0, 0, 0, 0, 0};
        ByteOrderMark customBOM = new ByteOrderMark(charsetName, bytes);
        
        // When
        String stringRepresentation = customBOM.toString();
        
        // Then
        String expected = "ByteOrderMark[TEST_CHARSET: 0x0,0x0,0x0,0x0,0x0,0x0]";
        assertEquals("String representation should match expected format", expected, stringRepresentation);
    }
    
    @Test
    public void testHashCode_ShouldNotThrowException() {
        // Given
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        
        // When & Then (should not throw)
        utf8.hashCode();
    }
    
    @Test
    public void testGet_WithZeroIndex_ShouldReturnFirstByte() {
        // Given
        ByteOrderMark customBOM = new ByteOrderMark("TEST", new int[]{42, 1, 2, 3, 4, 5, 6, 7});
        
        // When
        int firstByte = customBOM.get(0);
        
        // Then
        assertEquals("Should return first byte", 42, firstByte);
    }
}