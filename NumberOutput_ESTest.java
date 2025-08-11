package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for NumberOutput.
 *
 * Test groups:
 * - toString() for ints/longs/floats/doubles (incl. fast-writer variants)
 * - Writing numbers into byte[] and char[] buffers (success paths and failures)
 * - notFinite() helpers for float/double
 * - divBy1000() sanity checks
 */
public class NumberOutputTest {

    // -----------------------------------------------------------------------------
    // toString(): ints and longs
    // -----------------------------------------------------------------------------

    @Test
    public void toString_int_basicValues() {
        assertEquals("0", NumberOutput.toString(0));
        assertEquals("1", NumberOutput.toString(1));
        assertEquals("-1", NumberOutput.toString(-1));
        assertEquals("-1400", NumberOutput.toString(-1400));
        assertEquals("2420", NumberOutput.toString(2420));
    }

    @Test
    public void toString_long_boundaries() {
        assertEquals("2147483647", NumberOutput.toString(2147483647L));
        assertEquals("-2147483648", NumberOutput.toString(-2147483648L));
        assertEquals("-9223372036854775805", NumberOutput.toString(-9223372036854775805L));
        assertEquals("2574686535532678828", NumberOutput.toString(2574686535532678828L));
    }

    // -----------------------------------------------------------------------------
    // toString(): doubles and floats (also fast-writer parity)
    // -----------------------------------------------------------------------------

    @Test
    public void toString_double_matchesJavaFormatting() {
        double[] values = {0.0, 1.0, -1.0, 1.5, -2304.1, 9.007199254740992E18};
        for (double v : values) {
            // Default path
            assertEquals(Double.toString(v), NumberOutput.toString(v));
            // Fast-writer path
            assertEquals(Double.toString(v), NumberOutput.toString(v, true));
        }
    }

    @Test
    public void toString_float_matchesJavaFormatting() {
        float[] values = {0.0f, 1.0f, -1.0f, 1.5f, -310.243f, -2304.1f};
        for (float v : values) {
            // Default path
            assertEquals(Float.toString(v), NumberOutput.toString(v));
            // Fast-writer path
            assertEquals(Float.toString(v), NumberOutput.toString(v, true));
        }
    }

    // -----------------------------------------------------------------------------
    // notFinite(): doubles and floats
    // -----------------------------------------------------------------------------

    @Test
    public void notFinite_double() {
        assertTrue(NumberOutput.notFinite(Double.NaN));
        assertTrue(NumberOutput.notFinite(Double.POSITIVE_INFINITY));
        assertTrue(NumberOutput.notFinite(Double.NEGATIVE_INFINITY));
        assertFalse(NumberOutput.notFinite(0.0));
        assertFalse(NumberOutput.notFinite(-128.0));
    }

    @Test
    public void notFinite_float() {
        assertTrue(NumberOutput.notFinite(Float.NaN));
        assertTrue(NumberOutput.notFinite(Float.POSITIVE_INFINITY));
        assertTrue(NumberOutput.notFinite(Float.NEGATIVE_INFINITY));
        assertFalse(NumberOutput.notFinite(2650.0F));
        assertFalse(NumberOutput.notFinite(-128.0F));
    }

    // -----------------------------------------------------------------------------
    // divBy1000(): sanity checks (should match integer division semantics)
    // -----------------------------------------------------------------------------

    @Test
    public void divBy1000_behavesLikeIntegerDivision() {
        assertEquals(0, NumberOutput.divBy1000(0));
        assertEquals(0, NumberOutput.divBy1000(999));
        assertEquals(1, NumberOutput.divBy1000(1000));
        assertEquals(1, NumberOutput.divBy1000(1999));
        assertEquals(-1, NumberOutput.divBy1000(-1000));
        assertEquals(0, NumberOutput.divBy1000(-999));   // truncates toward zero
        assertEquals(-2, NumberOutput.divBy1000(-2001)); // truncates toward zero
    }

    // -----------------------------------------------------------------------------
    // outputInt(): writing to byte[] buffers
    // -----------------------------------------------------------------------------

    @Test
    public void outputInt_writesToByteArray_fromOffset0() {
        byte[] out = new byte[16];
        int end = NumberOutput.outputInt(1497, out, 0);
        assertEquals(4, end);
        assertEquals("1497", slice(out, 0, end));
    }

    @Test
    public void outputInt_writesToByteArray_atGivenOffset() {
        byte[] out = new byte[16];
        int start = 2;
        int end = NumberOutput.outputInt(1497, out, start);
        assertEquals(start + 4, end);
        assertEquals("1497", slice(out, start, 4));
    }

    @Test
    public void outputInt_writesMinIntToByteArray_exactSize() {
        String expected = Integer.toString(Integer.MIN_VALUE);
        byte[] out = new byte[expected.length()];
        int end = NumberOutput.outputInt(Integer.MIN_VALUE, out, 0);
        assertEquals(expected.length(), end);
        assertEquals(expected, slice(out, 0, end));
    }

    @Test
    public void outputInt_throwsOnNullByteArray() {
        assertThrows(NullPointerException.class, () -> NumberOutput.outputInt(10, (byte[]) null, 0));
    }

    @Test
    public void outputInt_throwsWhenByteArrayTooSmall() {
        // "2147483647" needs 10 bytes
        byte[] small = new byte[5];
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> NumberOutput.outputInt(2147483647, small, 0));
    }

    // -----------------------------------------------------------------------------
    // outputInt(): writing to char[] buffers
    // -----------------------------------------------------------------------------

    @Test
    public void outputInt_writesToCharArray_fromOffset0() {
        char[] out = new char[8];
        int end = NumberOutput.outputInt(0, out, 0);
        assertEquals(1, end);
        assertEquals("0", new String(out, 0, end));
    }

    @Test
    public void outputInt_writesNegativeToCharArray() {
        char[] out = new char[8];
        int end = NumberOutput.outputInt(-3640, out, 0);
        assertEquals(5, end);
        assertEquals("-3640", new String(out, 0, end));
    }

    @Test
    public void outputInt_throwsOnNullCharArray() {
        assertThrows(NullPointerException.class, () -> NumberOutput.outputInt(12, (char[]) null, 0));
    }

    @Test
    public void outputInt_throwsWhenCharArrayTooSmall() {
        // "1000000000" needs 10 chars
        char[] small = new char[7];
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> NumberOutput.outputInt(1_000_000_000, small, 0));
    }

    // -----------------------------------------------------------------------------
    // outputLong(): writing to byte[] buffers
    // -----------------------------------------------------------------------------

    @Test
    public void outputLong_writesPositiveToByteArray() {
        long value = 2_084_322_364L;
        String expected = Long.toString(value);
        byte[] out = new byte[expected.length() + 2];
        int start = 0;
        int end = NumberOutput.outputLong(value, out, start);
        assertEquals(expected.length(), end - start);
        assertEquals(expected, slice(out, start, end - start));
    }

    @Test
    public void outputLong_writesNegativeToByteArray() {
        long value = -424L;
        String expected = Long.toString(value);
        byte[] out = new byte[expected.length()];
        int end = NumberOutput.outputLong(value, out, 0);
        assertEquals(expected.length(), end);
        assertEquals(expected, slice(out, 0, end));
    }

    @Test
    public void outputLong_throwsOnNullByteArray() {
        assertThrows(NullPointerException.class, () -> NumberOutput.outputLong(1000L, (byte[]) null, 0));
    }

    @Test
    public void outputLong_throwsWhenByteArrayTooSmall() {
        // "7174648137343063403" needs 19 bytes
        byte[] small = new byte[5];
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> NumberOutput.outputLong(7_174_648_137_343_063_403L, small, 0));
    }

    // -----------------------------------------------------------------------------
    // outputLong(): writing to char[] buffers
    // -----------------------------------------------------------------------------

    @Test
    public void outputLong_writesZeroToCharArray_atOffset() {
        char[] out = new char[4];
        int start = 1;
        int end = NumberOutput.outputLong(0L, out, start);
        assertEquals(start + 1, end);
        assertEquals("0", new String(out, start, 1));
    }

    @Test
    public void outputLong_writesNegativeToCharArray() {
        long value = -3_640L;
        char[] out = new char[8];
        int end = NumberOutput.outputLong(value, out, 0);
        assertEquals(Long.toString(value), new String(out, 0, end));
    }

    @Test
    public void outputLong_throwsOnNullCharArray() {
        assertThrows(NullPointerException.class, () -> NumberOutput.outputLong(1L, (char[]) null, 0));
    }

    @Test
    public void outputLong_throwsWhenCharArrayTooSmall() {
        // "-2147483648" needs 11 chars
        char[] small = new char[9];
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> NumberOutput.outputLong(-2147483648L, small, 0));
    }

    // -----------------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------------

    private static String slice(byte[] b, int off, int len) {
        return new String(b, off, len, StandardCharsets.US_ASCII);
    }
}