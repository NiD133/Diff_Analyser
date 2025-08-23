package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for IOCase focusing on readability and intent.
 */
class IOCaseTest {

    // OS-dependent behavior: Windows is case-insensitive for filenames.
    private static final boolean IS_WINDOWS = File.separatorChar == '\\';

    // Common test data
    private static final String UPPER = "ABC";
    private static final String LOWER = "abc";
    private static final String MIXED_END = "Bc";
    private static final String MIXED_START = "Ab";
    private static final String DEF = "DEF";
    private static final String LONG = "ABCDEFGHIJ";

    private static void assertAllZero(final byte[] arr) {
        for (final byte e : arr) {
            assertEquals(0, e);
        }
    }

    private static void assertAllZero(final char[] arr) {
        for (final char e : arr) {
            assertEquals(0, e);
        }
    }

    private static IOCase roundTripSerialize(final IOCase value) throws Exception {
        final byte[] bytes;
        try (ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(outBytes)) {
            out.writeObject(value);
            out.flush();
            bytes = outBytes.toByteArray();
        }
        try (ByteArrayInputStream inBytes = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(inBytes)) {
            return (IOCase) in.readObject();
        }
    }

    // -----------------------------
    // checkCompareTo
    // -----------------------------

    @Test
    @DisplayName("checkCompareTo: case sensitivity")
    void checkCompareTo_caseSensitivity() {
        // Case-sensitive
        assertEquals(0, IOCase.SENSITIVE.checkCompareTo(UPPER, UPPER));
        assertTrue(IOCase.SENSITIVE.checkCompareTo(UPPER, LOWER) < 0);
        assertTrue(IOCase.SENSITIVE.checkCompareTo(LOWER, UPPER) > 0);

        // Case-insensitive
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo(UPPER, UPPER));
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo(UPPER, LOWER));
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo(LOWER, UPPER));

        // System-dependent
        assertEquals(0, IOCase.SYSTEM.checkCompareTo(UPPER, UPPER));
        assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkCompareTo(UPPER, LOWER) == 0);
        assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkCompareTo(LOWER, UPPER) == 0);
    }

    @Test
    @DisplayName("checkCompareTo: general behavior and nulls")
    void checkCompareTo_generalBehavior() {
        assertTrue(IOCase.SENSITIVE.checkCompareTo(UPPER, "") > 0);
        assertTrue(IOCase.SENSITIVE.checkCompareTo("", UPPER) < 0);
        assertTrue(IOCase.SENSITIVE.checkCompareTo(UPPER, DEF) < 0);
        assertTrue(IOCase.SENSITIVE.checkCompareTo(DEF, UPPER) > 0);
        assertEquals(0, IOCase.SENSITIVE.checkCompareTo(UPPER, UPPER));
        assertEquals(0, IOCase.SENSITIVE.checkCompareTo("", ""));

        assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(UPPER, null));
        assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(null, UPPER));
        assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(null, null));
    }

    // -----------------------------
    // checkEndsWith
    // -----------------------------

    @Test
    @DisplayName("checkEndsWith: case sensitivity")
    void checkEndsWith_caseSensitivity() {
        // Case-sensitive
        assertTrue(IOCase.SENSITIVE.checkEndsWith(UPPER, "BC"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(UPPER, MIXED_END));

        // Case-insensitive
        assertTrue(IOCase.INSENSITIVE.checkEndsWith(UPPER, "BC"));
        assertTrue(IOCase.INSENSITIVE.checkEndsWith(UPPER, MIXED_END));

        // System-dependent
        assertTrue(IOCase.SYSTEM.checkEndsWith(UPPER, "BC"));
        // On Windows: true, on Unix-like: false
        assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkEndsWith(UPPER, MIXED_END));
    }

    @Test
    @DisplayName("checkEndsWith: general behavior and nulls")
    void checkEndsWith_generalBehavior() {
        assertTrue(IOCase.SENSITIVE.checkEndsWith(UPPER, ""));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(UPPER, "A"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(UPPER, "AB"));
        assertTrue(IOCase.SENSITIVE.checkEndsWith(UPPER, "ABC"));
        assertTrue(IOCase.SENSITIVE.checkEndsWith(UPPER, "BC"));
        assertTrue(IOCase.SENSITIVE.checkEndsWith(UPPER, "C"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(UPPER, "ABCD"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith("", UPPER));
        assertTrue(IOCase.SENSITIVE.checkEndsWith("", ""));

        // Null-safe: returns false
        assertFalse(IOCase.SENSITIVE.checkEndsWith(UPPER, null));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(null, UPPER));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(null, null));
    }

    // -----------------------------
    // checkEquals
    // -----------------------------

    @Test
    @DisplayName("checkEquals: case sensitivity")
    void checkEquals_caseSensitivity() {
        // Case-sensitive
        assertTrue(IOCase.SENSITIVE.checkEquals(UPPER, UPPER));
        assertFalse(IOCase.SENSITIVE.checkEquals(UPPER, "Abc"));

        // Case-insensitive
        assertTrue(IOCase.INSENSITIVE.checkEquals(UPPER, UPPER));
        assertTrue(IOCase.INSENSITIVE.checkEquals(UPPER, "Abc"));

        // System-dependent
        assertTrue(IOCase.SYSTEM.checkEquals(UPPER, UPPER));
        assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkEquals(UPPER, "Abc"));
    }

    @Test
    @DisplayName("checkEquals: general behavior and nulls")
    void checkEquals_generalBehavior() {
        assertFalse(IOCase.SENSITIVE.checkEquals(UPPER, ""));
        assertFalse(IOCase.SENSITIVE.checkEquals(UPPER, "A"));
        assertFalse(IOCase.SENSITIVE.checkEquals(UPPER, "AB"));
        assertTrue(IOCase.SENSITIVE.checkEquals(UPPER, UPPER));
        assertFalse(IOCase.SENSITIVE.checkEquals(UPPER, "BC"));
        assertFalse(IOCase.SENSITIVE.checkEquals(UPPER, "C"));
        assertFalse(IOCase.SENSITIVE.checkEquals(UPPER, "ABCD"));
        assertFalse(IOCase.SENSITIVE.checkEquals("", UPPER));
        assertTrue(IOCase.SENSITIVE.checkEquals("", ""));

        // Null-safe: two nulls are considered equal, otherwise false
        assertFalse(IOCase.SENSITIVE.checkEquals(UPPER, null));
        assertFalse(IOCase.SENSITIVE.checkEquals(null, UPPER));
        assertTrue(IOCase.SENSITIVE.checkEquals(null, null));
    }

    // -----------------------------
    // checkIndexOf
    // -----------------------------

    @Test
    @DisplayName("checkIndexOf: case sensitivity and nulls")
    void checkIndexOf_caseSensitivity() {
        assertEquals(1, IOCase.SENSITIVE.checkIndexOf(UPPER, 0, "BC"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(UPPER, 0, MIXED_END));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, MIXED_END));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, null));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(UPPER, 0, null));

        assertEquals(1, IOCase.INSENSITIVE.checkIndexOf(UPPER, 0, "BC"));
        assertEquals(1, IOCase.INSENSITIVE.checkIndexOf(UPPER, 0, MIXED_END));

        assertEquals(1, IOCase.SYSTEM.checkIndexOf(UPPER, 0, "BC"));
        assertEquals(IS_WINDOWS ? 1 : -1, IOCase.SYSTEM.checkIndexOf(UPPER, 0, MIXED_END));
    }

    @Test
    @DisplayName("checkIndexOf: general behavior")
    void checkIndexOf_generalBehavior() {
        // Start
        assertEquals(0, IOCase.SENSITIVE.checkIndexOf(LONG, 0, "A"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG, 1, "A"));
        assertEquals(0, IOCase.SENSITIVE.checkIndexOf(LONG, 0, "AB"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG, 1, "AB"));
        assertEquals(0, IOCase.SENSITIVE.checkIndexOf(LONG, 0, "ABC"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG, 1, "ABC"));

        // Middle
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf(LONG, 0, "D"));
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf(LONG, 3, "D"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG, 4, "D"));
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf(LONG, 0, "DE"));
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf(LONG, 3, "DE"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG, 4, "DE"));
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf(LONG, 0, "DEF"));
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf(LONG, 3, "DEF"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG, 4, "DEF"));

        // End
        assertEquals(9, IOCase.SENSITIVE.checkIndexOf(LONG, 0, "J"));
        assertEquals(9, IOCase.SENSITIVE.checkIndexOf(LONG, 8, "J"));
        assertEquals(9, IOCase.SENSITIVE.checkIndexOf(LONG, 9, "J"));
        assertEquals(8, IOCase.SENSITIVE.checkIndexOf(LONG, 0, "IJ"));
        assertEquals(8, IOCase.SENSITIVE.checkIndexOf(LONG, 8, "IJ"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG, 9, "IJ"));
        assertEquals(7, IOCase.SENSITIVE.checkIndexOf(LONG, 6, "HIJ"));
        assertEquals(7, IOCase.SENSITIVE.checkIndexOf(LONG, 7, "HIJ"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG, 8, "HIJ"));

        // Not found
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG, 0, "DED"));

        // Search longer than target
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("DEF", 0, LONG));

        // Null-safe
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(UPPER, 0, null));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, UPPER));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, null));
    }

    // -----------------------------
    // checkRegionMatches
    // -----------------------------

    @Test
    @DisplayName("checkRegionMatches: case sensitivity")
    void checkRegionMatches_caseSensitivity() {
        assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPER, 0, "AB"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 0, MIXED_START));

        assertTrue(IOCase.INSENSITIVE.checkRegionMatches(UPPER, 0, "AB"));
        assertTrue(IOCase.INSENSITIVE.checkRegionMatches(UPPER, 0, MIXED_START));

        assertTrue(IOCase.SYSTEM.checkRegionMatches(UPPER, 0, "AB"));
        assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkRegionMatches(UPPER, 0, MIXED_START));
    }

    @Test
    @DisplayName("checkRegionMatches: general behavior and nulls")
    void checkRegionMatches_generalBehavior() {
        // At index 0
        assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPER, 0, ""));
        assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPER, 0, "A"));
        assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPER, 0, "AB"));
        assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPER, 0, "ABC"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 0, "BC"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 0, "C"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 0, "ABCD"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("", 0, UPPER));
        assertTrue(IOCase.SENSITIVE.checkRegionMatches("", 0, ""));

        // At index 1
        assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPER, 1, ""));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 1, "A"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 1, "AB"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 1, "ABC"));
        assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPER, 1, "BC"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 1, "C"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 1, "ABCD"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("", 1, UPPER));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("", 1, ""));

        // Null-safe
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 0, null));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, UPPER));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, null));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPER, 1, null));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 1, UPPER));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 1, null));
    }

    // -----------------------------
    // checkStartsWith
    // -----------------------------

    @Test
    @DisplayName("checkStartsWith: case sensitivity")
    void checkStartsWith_caseSensitivity() {
        assertTrue(IOCase.SENSITIVE.checkStartsWith(UPPER, "AB"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(UPPER, MIXED_START));

        assertTrue(IOCase.INSENSITIVE.checkStartsWith(UPPER, "AB"));
        assertTrue(IOCase.INSENSITIVE.checkStartsWith(UPPER, MIXED_START));

        assertTrue(IOCase.SYSTEM.checkStartsWith(UPPER, "AB"));
        assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkStartsWith(UPPER, MIXED_START));
    }

    @Test
    @DisplayName("checkStartsWith: general behavior and nulls")
    void checkStartsWith_generalBehavior() {
        assertTrue(IOCase.SENSITIVE.checkStartsWith(UPPER, ""));
        assertTrue(IOCase.SENSITIVE.checkStartsWith(UPPER, "A"));
        assertTrue(IOCase.SENSITIVE.checkStartsWith(UPPER, "AB"));
        assertTrue(IOCase.SENSITIVE.checkStartsWith(UPPER, "ABC"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(UPPER, "BC"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(UPPER, "C"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(UPPER, "ABCD"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith("", UPPER));
        assertTrue(IOCase.SENSITIVE.checkStartsWith("", ""));

        // Null-safe
        assertFalse(IOCase.SENSITIVE.checkStartsWith(UPPER, null));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(null, UPPER));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(null, null));
    }

    // -----------------------------
    // Simple API tests
    // -----------------------------

    @Test
    void forName_returnsEnumOrThrows() {
        assertEquals(IOCase.SENSITIVE, IOCase.forName("Sensitive"));
        assertEquals(IOCase.INSENSITIVE, IOCase.forName("Insensitive"));
        assertEquals(IOCase.SYSTEM, IOCase.forName("System"));
        assertThrows(IllegalArgumentException.class, () -> IOCase.forName("Blah"));
        assertThrows(IllegalArgumentException.class, () -> IOCase.forName(null));
    }

    @Test
    void getName_matchesToStringAndEnumNames() {
        assertEquals("Sensitive", IOCase.SENSITIVE.getName());
        assertEquals("Insensitive", IOCase.INSENSITIVE.getName());
        assertEquals("System", IOCase.SYSTEM.getName());
        assertEquals("Sensitive", IOCase.SENSITIVE.toString());
        assertEquals("Insensitive", IOCase.INSENSITIVE.toString());
        assertEquals("System", IOCase.SYSTEM.toString());
    }

    @Test
    void isCaseSensitive_instanceAndStatic() {
        assertTrue(IOCase.SENSITIVE.isCaseSensitive());
        assertFalse(IOCase.INSENSITIVE.isCaseSensitive());
        assertEquals(!IS_WINDOWS, IOCase.SYSTEM.isCaseSensitive());

        assertTrue(IOCase.isCaseSensitive(IOCase.SENSITIVE));
        assertFalse(IOCase.isCaseSensitive(IOCase.INSENSITIVE));
        assertEquals(!IS_WINDOWS, IOCase.isCaseSensitive(IOCase.SYSTEM));
    }

    @Test
    void serialization_roundTripReturnsSameEnumConstant() throws Exception {
        assertSame(IOCase.SENSITIVE, roundTripSerialize(IOCase.SENSITIVE));
        assertSame(IOCase.INSENSITIVE, roundTripSerialize(IOCase.INSENSITIVE));
        assertSame(IOCase.SYSTEM, roundTripSerialize(IOCase.SYSTEM));
    }

    // -----------------------------
    // IOUtils scratch array tests (kept here for historical reasons)
    // -----------------------------

    @Test
    void getScratchByteArray_returnsZeroedArrayEachCall() {
        final byte[] first = IOUtils.getScratchByteArray();
        assertAllZero(first);
        Arrays.fill(first, (byte) 1);

        // A fresh call should return an array zeroed out
        assertAllZero(IOUtils.getScratchByteArray());
    }

    @Test
    void getScratchByteArrayWriteOnly_returnsZeroedArrayEachCall() {
        final byte[] first = IOUtils.getScratchByteArrayWriteOnly();
        assertAllZero(first);
        Arrays.fill(first, (byte) 1);

        assertAllZero(IOUtils.getScratchByteArrayWriteOnly());
    }

    @Test
    void getScratchCharArray_returnsZeroedArrayEachCall() {
        final char[] first = IOUtils.getScratchCharArray();
        assertAllZero(first);
        Arrays.fill(first, (char) 1);

        assertAllZero(IOUtils.getScratchCharArray());
    }

    @Test
    void getScratchCharArrayWriteOnly_returnsZeroedArrayEachCall() {
        final char[] first = IOUtils.getScratchCharArrayWriteOnly();
        assertAllZero(first);
        Arrays.fill(first, (char) 1);

        assertAllZero(IOUtils.getScratchCharArrayWriteOnly());
    }
}