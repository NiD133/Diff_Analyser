package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.NumberOutput;

/**
 * Test suite for NumberOutput class functionality.
 * Tests number formatting, buffer operations, and edge cases.
 */
public class NumberOutputTest {

    // ========== String Conversion Tests ==========
    
    @Test
    public void testToString_integers() {
        assertEquals("0", NumberOutput.toString(0));
        assertEquals("1", NumberOutput.toString(1));
        assertEquals("11", NumberOutput.toString(11));
        assertEquals("2420", NumberOutput.toString(2420));
        assertEquals("-1", NumberOutput.toString(-1));
        assertEquals("-1400", NumberOutput.toString(-1400));
    }

    @Test
    public void testToString_integerBoundaries() {
        assertEquals("-2147483648", NumberOutput.toString(Integer.MIN_VALUE));
        assertEquals("2147483647", NumberOutput.toString(Integer.MAX_VALUE));
    }

    @Test
    public void testToString_longs() {
        assertEquals("1", NumberOutput.toString(1L));
        assertEquals("11", NumberOutput.toString(11L));
        assertEquals("-11", NumberOutput.toString(-11L));
        assertEquals("2574686535532678828", NumberOutput.toString(2574686535532678828L));
        assertEquals("-9223372036854775805", NumberOutput.toString(-9223372036854775805L));
    }

    @Test
    public void testToString_longBoundaries() {
        assertEquals("-2147483648", NumberOutput.toString(-2147483648L));
        assertEquals("2147483647", NumberOutput.toString(2147483647L));
    }

    @Test
    public void testToString_floats() {
        assertEquals("1.0", NumberOutput.toString(1.0F));
        assertEquals("-310.243", NumberOutput.toString(-310.243F));
    }

    @Test
    public void testToString_floatsWithFastWriter() {
        assertEquals("1.0", NumberOutput.toString(1.0F, true));
    }

    @Test
    public void testToString_doubles() {
        assertEquals("1.0", NumberOutput.toString(1.0));
        assertEquals("-1.0", NumberOutput.toString(-1.0));
        assertEquals("-2304.1", NumberOutput.toString(-2304.1F, false));
    }

    @Test
    public void testToString_doublesWithFastWriter() {
        assertEquals("9.007199254740992E18", NumberOutput.toString(9.007199254740992E18, true));
    }

    // ========== Finite Number Validation Tests ==========
    
    @Test
    public void testNotFinite_validNumbers() {
        assertFalse("Regular float should be finite", NumberOutput.notFinite(2650.0F));
        assertFalse("Negative number should be finite", NumberOutput.notFinite(-128.0));
    }

    // ========== Buffer Output Tests - Success Cases ==========
    
    @Test
    public void testOutputInt_toCharArray_zero() {
        char[] buffer = new char[5];
        int endIndex = NumberOutput.outputInt(0, buffer, 0);
        
        assertEquals("Should return index after last written character", 1, endIndex);
        assertEquals("Should write '0' at start", '0', buffer[0]);
    }

    @Test
    public void testOutputInt_toByteArray_zero() {
        byte[] buffer = new byte[5];
        int endIndex = NumberOutput.outputInt(0, buffer, 0);
        
        assertEquals("Should return index after last written character", 1, endIndex);
        assertEquals("Should write '0' byte", (byte)'0', buffer[0]);
    }

    @Test
    public void testOutputInt_toByteArray_multiDigit() {
        byte[] buffer = new byte[6];
        int endIndex = NumberOutput.outputInt(1497, buffer, 2);
        
        assertEquals("Should return index after last written character", 6, endIndex);
        assertArrayEquals("Should write digits starting at offset 2", 
            new byte[] {0, 0, (byte)'1', (byte)'4', (byte)'9', (byte)'7'}, buffer);
    }

    @Test
    public void testOutputLong_toCharArray_zero() {
        char[] buffer = new char[7];
        int endIndex = NumberOutput.outputLong(0L, buffer, 1);
        
        assertEquals("Should return index after last written character", 2, endIndex);
        assertEquals("Should write '0' at offset 1", '0', buffer[1]);
    }

    @Test
    public void testOutputLong_toCharArray_negative() {
        char[] buffer = new char[6];
        int endIndex = NumberOutput.outputLong(-3640L, buffer, 0);
        
        assertEquals("Should return index after last written character", 5, endIndex);
        assertArrayEquals("Should write negative number with minus sign", 
            new char[] {'-', '3', '6', '4', '0', '\u0000'}, buffer);
    }

    @Test
    public void testOutputLong_toByteArray_singleDigit() {
        byte[] buffer = new byte[2];
        int endIndex = NumberOutput.outputLong(1L, buffer, 0);
        
        assertEquals("Should return index after last written character", 1, endIndex);
        assertArrayEquals("Should write single digit", 
            new byte[] {(byte)'1', (byte)0}, buffer);
    }

    @Test
    public void testOutputLong_toCharArray_largeNumber() {
        char[] buffer = new char[14];
        int endIndex = NumberOutput.outputLong(2084322237L, buffer, 4);
        
        assertEquals("Should return index after last written character", 14, endIndex);
    }

    @Test
    public void testOutputLong_toByteArray_largeNumber() {
        byte[] buffer = new byte[25];
        int endIndex = NumberOutput.outputLong(2084322364L, buffer, 0);
        
        assertEquals("Should return index after last written character", 10, endIndex);
    }

    // ========== Null Pointer Exception Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void testOutputInt_nullCharArray() {
        NumberOutput.outputInt(10, (char[]) null, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testOutputInt_nullByteArray() {
        NumberOutput.outputInt(10, (byte[]) null, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testOutputLong_nullCharArray() {
        NumberOutput.outputLong(1L, (char[]) null, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testOutputLong_nullByteArray() {
        NumberOutput.outputLong(99L, (byte[]) null, 91);
    }

    // ========== Array Index Out of Bounds Tests ==========
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutputInt_charArray_negativeOffset() {
        char[] buffer = new char[5];
        NumberOutput.outputInt(1000000, buffer, -413);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutputInt_charArray_offsetTooLarge() {
        char[] buffer = new char[5];
        NumberOutput.outputInt(1000000, buffer, 5);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutputInt_byteArray_offsetTooLarge() {
        byte[] buffer = new byte[5];
        NumberOutput.outputInt(1000000, buffer, 11);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutputInt_bufferTooSmall() {
        byte[] buffer = new byte[1];
        NumberOutput.outputInt(Integer.MAX_VALUE, buffer, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutputLong_charArray_negativeOffset() {
        char[] buffer = new char[8];
        NumberOutput.outputLong(9007199254740992000L, buffer, -2118);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutputLong_charArray_offsetTooLarge() {
        char[] buffer = new char[7];
        NumberOutput.outputLong(10000000000000L, buffer, 340);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutputLong_byteArray_negativeOffset() {
        byte[] buffer = new byte[3];
        NumberOutput.outputLong(9164449253911987585L, buffer, -48);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutputLong_byteArray_offsetTooLarge() {
        byte[] buffer = new byte[21];
        NumberOutput.outputLong(1000000000000L, buffer, 1668);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutputLong_bufferTooSmallForLargeNumber() {
        byte[] buffer = new byte[5];
        NumberOutput.outputLong(1000000000L, buffer, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutputLong_bufferTooSmallForNegativeNumber() {
        char[] buffer = new char[9];
        NumberOutput.outputLong(Integer.MIN_VALUE, buffer, 0);
    }

    // ========== Utility Method Tests ==========
    
    @Test
    public void testDivBy1000_zero() {
        assertEquals("Division of 0 by 1000 should be 0", 0, NumberOutput.divBy1000(0));
    }

    @Test
    public void testDivBy1000_negative() {
        assertEquals("Division should handle negative numbers", 67108862, NumberOutput.divBy1000(-1652));
    }

    // ========== Constructor Test ==========
    
    @Test
    public void testConstructor() {
        // Verify that NumberOutput can be instantiated (though it's a utility class)
        NumberOutput numberOutput = new NumberOutput();
        assertNotNull("Should be able to create NumberOutput instance", numberOutput);
    }
}