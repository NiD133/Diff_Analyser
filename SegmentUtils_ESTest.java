package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.harmony.unpack200.IMatcher;
import org.apache.commons.compress.harmony.unpack200.SegmentUtils;

/**
 * Test suite for SegmentUtils utility methods.
 * Tests argument counting, bit flag operations, and matcher functionality.
 */
public class SegmentUtilsTest {

    // ========== Argument Counting Tests ==========
    
    @Test
    public void countArgs_withValidMethodDescriptor_returnsCorrectCount() {
        // Method descriptor with multiple arguments
        String descriptor = "&L(LkEf;|)7g<";
        
        int argumentCount = SegmentUtils.countArgs(descriptor);
        
        assertEquals("Should count 2 arguments in descriptor", 2, argumentCount);
    }
    
    @Test
    public void countArgs_withEmptyParentheses_returnsZero() {
        String descriptorWithEmptyArgs = "1.8()JbKnQPeYyNxq!";
        
        int argumentCount = SegmentUtils.countArgs(descriptorWithEmptyArgs);
        
        assertEquals("Empty parentheses should result in 0 arguments", 0, argumentCount);
    }
    
    @Test
    public void countArgs_withSpecificWidthForLongsAndDoubles_appliesCorrectMultiplier() {
        String descriptor = "Can't read beyond end of stream (n = %,d, count = %,d, maxLength = %,d, remaining  %,d)";
        int widthMultiplier = 103;
        
        int result = SegmentUtils.countArgs(descriptor, widthMultiplier);
        
        assertEquals("Should apply width multiplier correctly", 26, result);
    }
    
    @Test
    public void countArgs_withNegativeWidth_performsCalculation() {
        String descriptor = "l<<(6JNLi@g)";
        int negativeWidth = -1656;
        
        int result = SegmentUtils.countArgs(descriptor, negativeWidth);
        
        assertEquals("Should handle negative width", -1653, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void countArgs_withNullDescriptor_throwsNullPointerException() {
        SegmentUtils.countArgs(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void countArgs_withNullDescriptorAndWidth_throwsNullPointerException() {
        SegmentUtils.countArgs(null, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void countArgs_withInvalidDescriptor_throwsIllegalArgumentException() {
        // Descriptor without proper argument structure
        String invalidDescriptor = "tp!Hgy";
        
        SegmentUtils.countArgs(invalidDescriptor);
    }

    // ========== Interface Arguments Counting Tests ==========
    
    @Test
    public void countInvokeInterfaceArgs_withValidDescriptor_returnsCorrectCount() {
        String descriptor = "(X=K[}a)\"3/[Ns";
        
        int count = SegmentUtils.countInvokeInterfaceArgs(descriptor);
        
        assertEquals("Should count interface arguments correctly", 5, count);
    }
    
    @Test
    public void countInvokeInterfaceArgs_withEmptyArgs_returnsZero() {
        String descriptorWithEmptyArgs = "1.8()JbKnQPeYyNxq!";
        
        int count = SegmentUtils.countInvokeInterfaceArgs(descriptorWithEmptyArgs);
        
        assertEquals("Empty arguments should return 0", 0, count);
    }
    
    @Test(expected = NullPointerException.class)
    public void countInvokeInterfaceArgs_withNullDescriptor_throwsNullPointerException() {
        SegmentUtils.countInvokeInterfaceArgs(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void countInvokeInterfaceArgs_withInvalidDescriptor_throwsIllegalArgumentException() {
        String invalidDescriptor = "Aw2<'N6_{7~h_K?(gZ";
        
        SegmentUtils.countInvokeInterfaceArgs(invalidDescriptor);
    }

    // ========== Bit Counting Tests ==========
    
    @Test
    public void countBit16_withIntArrayContainingBit16Values_returnsCorrectCount() {
        int[] flagsWithBit16Set = {-142}; // Negative value has bit 16 set
        
        int count = SegmentUtils.countBit16(flagsWithBit16Set);
        
        assertEquals("Should count 1 value with bit 16 set", 1, count);
    }
    
    @Test
    public void countBit16_withIntArrayOfZeros_returnsZero() {
        int[] emptyFlags = {0};
        
        int count = SegmentUtils.countBit16(emptyFlags);
        
        assertEquals("Array of zeros should return 0", 0, count);
    }
    
    @Test
    public void countBit16_withLongArrayContainingBit16Values_returnsCorrectCount() {
        long[] flags = new long[5];
        flags[1] = -1653L; // Negative value has bit 16 set
        
        int count = SegmentUtils.countBit16(flags);
        
        assertEquals("Should count 1 long value with bit 16 set", 1, count);
    }
    
    @Test
    public void countBit16_withLongArrayOfZeros_returnsZero() {
        long[] emptyFlags = new long[5];
        
        int count = SegmentUtils.countBit16(emptyFlags);
        
        assertEquals("Array of zeros should return 0", 0, count);
    }
    
    @Test
    public void countBit16_with2DLongArrayContainingBit16Values_returnsCorrectCount() {
        long[][] flags = new long[6][0];
        long[] innerArray = new long[6];
        innerArray[5] = 65536L; // Value with bit 16 set
        flags[4] = innerArray;
        
        int count = SegmentUtils.countBit16(flags);
        
        assertEquals("Should count 1 value with bit 16 set in 2D array", 1, count);
    }
    
    @Test
    public void countBit16_with2DLongArrayOfZeros_returnsZero() {
        long[][] flags = new long[6][0];
        flags[0] = new long[2]; // Array of zeros
        
        int count = SegmentUtils.countBit16(flags);
        
        assertEquals("2D array of zeros should return 0", 0, count);
    }
    
    @Test(expected = NullPointerException.class)
    public void countBit16_withNullIntArray_throwsNullPointerException() {
        SegmentUtils.countBit16((int[]) null);
    }
    
    @Test(expected = NullPointerException.class)
    public void countBit16_withNullLongArray_throwsNullPointerException() {
        SegmentUtils.countBit16((long[]) null);
    }
    
    @Test(expected = NullPointerException.class)
    public void countBit16_withNull2DLongArray_throwsNullPointerException() {
        SegmentUtils.countBit16((long[][]) null);
    }

    // ========== Matcher Tests ==========
    
    @Test
    public void countMatches_withEmptyLongArray_returnsZero() {
        long[] emptyFlags = new long[0];
        
        int matches = SegmentUtils.countMatches(emptyFlags, null);
        
        assertEquals("Empty array should return 0 matches", 0, matches);
    }
    
    @Test
    public void countMatches_withEmpty2DLongArray_returnsZero() {
        long[][] emptyFlags = new long[0][5];
        
        int matches = SegmentUtils.countMatches(emptyFlags, null);
        
        assertEquals("Empty 2D array should return 0 matches", 0, matches);
    }
    
    @Test(expected = NullPointerException.class)
    public void countMatches_withNonEmptyArrayAndNullMatcher_throwsNullPointerException() {
        long[] flags = new long[4];
        
        SegmentUtils.countMatches(flags, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void countMatches_withNonEmpty2DArrayAndNullMatcher_throwsNullPointerException() {
        long[][] flags = new long[4][7];
        flags[0] = new long[0];
        
        SegmentUtils.countMatches(flags, null);
    }

    // ========== Constructor Test ==========
    
    @Test
    public void constructor_canBeInstantiated() {
        // Verify that SegmentUtils can be instantiated (though it's a utility class)
        SegmentUtils segmentUtils = new SegmentUtils();
        
        assertNotNull("SegmentUtils instance should not be null", segmentUtils);
    }
}