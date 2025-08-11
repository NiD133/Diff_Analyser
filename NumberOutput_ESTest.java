package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on clarity,
 * correctness, and coverage of edge cases.
 */
public class NumberOutputTest {

    // A buffer large enough for any long value (-Long.MIN_VALUE is 20 chars) plus some padding.
    private final char[] CHAR_BUFFER = new char[30];
    private final byte[] BYTE_BUFFER = new byte[30];

    // ==========================================================
    // Tests for toString(int)
    // ==========================================================

    @Test
    public void toString_withPositiveInt_shouldReturnCorrectString() {
        assertEquals("1", NumberOutput.toString(1));
        assertEquals("123", NumberOutput.toString(123));
        assertEquals("2147483647", NumberOutput.toString(Integer.MAX_VALUE));
    }

    @Test
    public void toString_withNegativeInt_shouldReturnCorrectString() {
        assertEquals("-1", NumberOutput.toString(-1));
        assertEquals("-1400", NumberOutput.toString(-1400));
        assertEquals("-2147483648", NumberOutput.toString(Integer.MIN_VALUE));
    }

    @Test
    public void toString_withZeroInt_shouldReturnZeroString() {
        assertEquals("0", NumberOutput.toString(0));
    }

    // ==========================================================
    // Tests for toString(long)
    // ==========================================================

    @Test
    public void toString_withPositiveLong_shouldReturnCorrectString() {
        assertEquals("11", NumberOutput.toString(11L));
        assertEquals("2574686535532678828", NumberOutput.toString(2574686535532678828L));
        assertEquals("9223372036854775807", NumberOutput.toString(Long.MAX_VALUE));
    }

    @Test
    public void toString_withNegativeLong_shouldReturnCorrectString() {
        assertEquals("-11", NumberOutput.toString(-11L));
        assertEquals("-9223372036854775805", NumberOutput.toString(-9223372036854775805L));
        assertEquals("-9223372036854775808", NumberOutput.toString(Long.MIN_VALUE));
    }

    @Test
    public void toString_withZeroLong_shouldReturnZeroString() {
        assertEquals("0", NumberOutput.toString(0L));
    }

    // ==========================================================
    // Tests for toString(double/float)
    // ==========================================================

    @Test
    public void toString_withSimpleDouble_shouldReturnDecimalString() {
        assertEquals("1.0", NumberOutput.toString(1.0));
        assertEquals("-1.0", NumberOutput.toString(-1.0));
    }

    @Test
    public void toString_withSimpleFloat_shouldReturnDecimalString() {
        assertEquals("-310.243", NumberOutput.toString(-310.243F));
        assertEquals("1.0", NumberOutput.toString(1.0F, true));
    }

    @Test
    public void toString_withLargeDoubleUsingFastWriter_shouldUseScientificNotation() {
        String expected = "9.007199254740992E18";
        assertEquals(expected, NumberOutput.toString(9007199254740992000.0, true));
    }

    // ==========================================================
    // Tests for notFinite()
    // ==========================================================

    @Test
    public void notFinite_withFiniteValues_shouldReturnFalse() {
        assertFalse(NumberOutput.notFinite(2650.0F));
        assertFalse(NumberOutput.notFinite(-128.0));
    }

    @Test
    public void notFinite_withInfiniteOrNaNValues_shouldReturnTrue() {
        assertTrue(NumberOutput.notFinite(Float.POSITIVE_INFINITY));
        assertTrue(NumberOutput.notFinite(Double.NEGATIVE_INFINITY));
        assertTrue(NumberOutput.notFinite(Float.NaN));
        assertTrue(NumberOutput.notFinite(Double.NaN));
    }

    // ==========================================================
    // Tests for outputInt() into char[]
    // ==========================================================

    @Test
    public void outputIntToChars_withPositiveInt_shouldWriteCorrectChars() {
        int nextOffset = NumberOutput.outputInt(1497, CHAR_BUFFER, 2);
        assertEquals(6, nextOffset); // 2 (offset) + 4 (digits)
        assertEquals("1497", new String(CHAR_BUFFER, 2, 4));
    }

    @Test
    public void outputIntToChars_withIntMin_shouldWriteCorrectChars() {
        int nextOffset = NumberOutput.outputInt(Integer.MIN_VALUE, CHAR_BUFFER, 0);
        String expected = Integer.toString(Integer.MIN_VALUE);
        assertEquals(expected.length(), nextOffset);
        assertEquals(expected, new String(CHAR_BUFFER, 0, nextOffset));
    }

    @Test(expected = NullPointerException.class)
    public void outputIntToChars_withNullBuffer_shouldThrowException() {
        NumberOutput.outputInt(12, null, 12);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputIntToChars_withNegativeOffset_shouldThrowException() {
        NumberOutput.outputInt(100, CHAR_BUFFER, -1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputIntToChars_withInsufficientSpace_shouldThrowException() {
        // "1000" requires 4 chars, but buffer from offset 7 has only 3 remaining.
        char[] smallBuffer = new char[10];
        NumberOutput.outputInt(1000, smallBuffer, 7);
    }

    // ==========================================================
    // Tests for outputLong() into byte[]
    // ==========================================================

    @Test
    public void outputLongToBytes_withSingleDigitLong_shouldWriteCorrectByte() {
        int nextOffset = NumberOutput.outputLong(1L, BYTE_BUFFER, 0);
        assertEquals(1, nextOffset);
        assertEquals((byte) '1', BYTE_BUFFER[0]);
    }

    @Test
    public void outputLongToBytes_withLargePositiveLong_shouldWriteCorrectBytes() {
        long val = 2084322364L;
        int nextOffset = NumberOutput.outputLong(val, BYTE_BUFFER, 0);
        assertEquals(10, nextOffset);
        assertEquals(String.valueOf(val), new String(BYTE_BUFFER, 0, 10));
    }

    @Test
    public void outputLongToBytes_withNegativeLong_shouldWriteCorrectBytes() {
        long val = -424L;
        int nextOffset = NumberOutput.outputLong(val, BYTE_BUFFER, 0);
        assertEquals(4, nextOffset); // 1 (sign) + 3 (digits)
        assertEquals(String.valueOf(val), new String(BYTE_BUFFER, 0, 4));
    }

    @Test(expected = NullPointerException.class)
    public void outputLongToBytes_withNullBuffer_shouldThrowException() {
        NumberOutput.outputLong(99L, null, 91);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongToBytes_withNegativeOffset_shouldThrowException() {
        NumberOutput.outputLong(123L, BYTE_BUFFER, -1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongToBytes_withInsufficientSpace_shouldThrowException() {
        // A 19-digit long requires 19 bytes, but buffer has only 5.
        byte[] smallBuffer = new byte[5];
        NumberOutput.outputLong(7174648137343063403L, smallBuffer, 0);
    }

    // ==========================================================
    // Miscellaneous Tests
    // ==========================================================

    @Test
    public void constructor_shouldCreateInstanceForCoverage() {
        // Test for code coverage of the default constructor.
        assertNotNull(new NumberOutput());
    }

    @Test
    public void divBy1000_withVariousInputs_shouldReturnCorrectIntegerDivision() {
        // This method may use an optimized calculation for integer division.
        // We test that it behaves according to standard integer division rules.
        assertEquals(0, NumberOutput.divBy1000(0));
        assertEquals(1, NumberOutput.divBy1000(1000));
        assertEquals(1, NumberOutput.divBy1000(1999));
        assertEquals(-1, NumberOutput.divBy1000(-1000));
        assertEquals(-1, NumberOutput.divBy1000(-1652));
    }
}