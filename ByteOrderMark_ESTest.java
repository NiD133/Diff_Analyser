package org.apache.commons.io;

import org.junit.Test;

import static org.junit.Assert.*;

public class ByteOrderMarkTest {

    // length()

    @Test
    public void length_utf8_is3() {
        assertEquals(3, ByteOrderMark.UTF_8.length());
    }

    // get(int)

    @Test
    public void get_validIndex_returnsExpectedValue() {
        // UTF-32BE bytes: 0x00 0x00 0xFE 0xFF
        assertEquals(255, ByteOrderMark.UTF_32BE.get(3));
    }

    @Test
    public void get_outOfBounds_throwsArrayIndexOutOfBoundsException() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ByteOrderMark.UTF_8.get(-5));
    }

    @Test
    public void get_returnsStoredNegativeValueVerbatim() {
        ByteOrderMark bom = new ByteOrderMark("Custom", 0, 0, -127, 0);
        assertEquals(-127, bom.get(2));
    }

    @Test
    public void get_defaultZeroAtIndex() {
        ByteOrderMark bom = new ByteOrderMark("U", new int[8]);
        assertEquals(0, bom.get(3));
    }

    // constructors and validation

    @Test
    public void constructor_nullBytes_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new ByteOrderMark("U", (int[]) null));
        // Implementation uses Objects.requireNonNull with the label "bytes"
        assertTrue(String.valueOf(ex.getMessage()).contains("bytes"));
    }

    @Test
    public void constructor_emptyCharset_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ByteOrderMark("", new int[3]));
        assertEquals("No charsetName specified", ex.getMessage());
    }

    @Test
    public void constructor_emptyBytes_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ByteOrderMark("Custom", new int[0]));
        assertEquals("No bytes specified", ex.getMessage());
    }

    // getCharsetName()

    @Test
    public void getCharsetName_returnsGivenName() {
        ByteOrderMark bom = new ByteOrderMark("My-Charset", 0, 1, 2);
        assertEquals("My-Charset", bom.getCharsetName());
    }

    // toString()

    @Test
    public void toString_formatsAsExpected() {
        ByteOrderMark bom = new ByteOrderMark("Custom", 0, 0, 0, 0, 0, 0);
        assertEquals("ByteOrderMark[Custom: 0x0,0x0,0x0,0x0,0x0,0x0]", bom.toString());
    }

    // getRawBytes() and matches(int[])

    @Test
    public void getRawBytes_andMatches_exactBytes_returnsTrue() {
        ByteOrderMark bom = new ByteOrderMark("X", 1, 2, 3);
        int[] raw = bom.getRawBytes();
        assertTrue(bom.matches(raw));
    }

    @Test
    public void matches_nullArray_returnsFalse() {
        assertFalse(ByteOrderMark.UTF_32BE.matches(null));
    }

    @Test
    public void matches_arrayShorterThanBom_returnsFalse() {
        assertFalse(ByteOrderMark.UTF_8.matches(new int[1]));
    }

    @Test
    public void matches_longerArrayThatDoesNotStartWithBom_returnsFalse() {
        assertFalse(ByteOrderMark.UTF_16BE.matches(new int[8])); // all zeros, does not start with 0xFE,0xFF
    }

    @Test
    public void matches_longerArrayWithBomPrefix_returnsTrue() {
        // UTF-16BE: 0xFE, 0xFF
        assertTrue(ByteOrderMark.UTF_16BE.matches(new int[] { 0xFE, 0xFF, 0x00, 0x01 }));
    }

    @Test
    public void matches_charBomCodePointValueDoesNotAccidentallyMatch() {
        // The Unicode BOM code point (0xFEFF) in a single int should not match a byte-sequence BOM.
        int[] data = new int[8];
        data[0] = ByteOrderMark.UTF_BOM; // 0xFEFF
        assertFalse(ByteOrderMark.UTF_16BE.matches(data));
    }

    @Test
    public void matches_customAllZeros_exactMatch_returnsTrue() {
        int[] zeros = new int[6];
        ByteOrderMark bom = new ByteOrderMark("Custom", zeros);
        assertTrue(bom.matches(zeros));
    }

    // getBytes()

    @Test
    public void getBytes_utf32le_returnsExpectedByteArray() {
        byte[] bytes = ByteOrderMark.UTF_32LE.getBytes();
        assertArrayEquals(new byte[] { (byte) 0xFF, (byte) 0xFE, 0x00, 0x00 }, bytes);
    }

    // equals()

    @Test
    public void equals_sameInstance_true() {
        assertTrue(ByteOrderMark.UTF_32LE.equals(ByteOrderMark.UTF_32LE));
    }

    @Test
    public void equals_differentConstants_false() {
        assertFalse(ByteOrderMark.UTF_16BE.equals(ByteOrderMark.UTF_16LE));
        assertFalse(ByteOrderMark.UTF_8.equals(ByteOrderMark.UTF_16BE));
        assertFalse(ByteOrderMark.UTF_32LE.equals(ByteOrderMark.UTF_32BE));
    }

    @Test
    public void equals_differentLength_false() {
        ByteOrderMark customDifferentLength = new ByteOrderMark("str", new int[7]);
        assertFalse(ByteOrderMark.UTF_32LE.equals(customDifferentLength));
    }

    @Test
    public void equals_nonByteOrderMarkObject_false() {
        ByteOrderMark bom = new ByteOrderMark("X", 0);
        assertFalse(bom.equals(new Object()));
    }

    // hashCode()

    @Test
    public void hashCode_canBeInvoked() {
        // No specific contract beyond Object; just ensure no exceptions.
        ByteOrderMark.UTF_8.hashCode();
    }
}