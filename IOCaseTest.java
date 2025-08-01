package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link IOCase}.
 */
class IOCaseTest {

    private static final boolean WINDOWS = File.separatorChar == '\\';

    private void assertArrayElementsAreZero(final byte[] arr) {
        for (final byte e : arr) {
            assertEquals(0, e);
        }
    }

    private void assertArrayElementsAreZero(final char[] arr) {
        for (final char e : arr) {
            assertEquals(0, e);
        }
    }

    private IOCase serializeIOCase(final IOCase value) throws Exception {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(buffer)) {
            out.writeObject(value);
            out.flush();
        }

        final ByteArrayInputStream inputBuffer = new ByteArrayInputStream(buffer.toByteArray());
        try (ObjectInputStream in = new ObjectInputStream(inputBuffer)) {
            return (IOCase) in.readObject();
        }
    }

    @Test
    void testCaseSensitiveComparison() {
        assertEquals(0, IOCase.SENSITIVE.checkCompareTo("ABC", "ABC"));
        assertTrue(IOCase.SENSITIVE.checkCompareTo("ABC", "abc") < 0);
        assertTrue(IOCase.SENSITIVE.checkCompareTo("abc", "ABC") > 0);
    }

    @Test
    void testCaseInsensitiveComparison() {
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("ABC", "ABC"));
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("ABC", "abc"));
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("abc", "ABC"));
    }

    @Test
    void testSystemCaseComparison() {
        assertEquals(0, IOCase.SYSTEM.checkCompareTo("ABC", "ABC"));
        assertEquals(WINDOWS, IOCase.SYSTEM.checkCompareTo("ABC", "abc") == 0);
        assertEquals(WINDOWS, IOCase.SYSTEM.checkCompareTo("abc", "ABC") == 0);
    }

    @Test
    void testCompareFunctionality() {
        assertTrue(IOCase.SENSITIVE.checkCompareTo("ABC", "") > 0);
        assertTrue(IOCase.SENSITIVE.checkCompareTo("", "ABC") < 0);
        assertTrue(IOCase.SENSITIVE.checkCompareTo("ABC", "DEF") < 0);
        assertTrue(IOCase.SENSITIVE.checkCompareTo("DEF", "ABC") > 0);
        assertEquals(0, IOCase.SENSITIVE.checkCompareTo("ABC", "ABC"));
        assertEquals(0, IOCase.SENSITIVE.checkCompareTo("", ""));

        assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo("ABC", null));
        assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(null, "ABC"));
        assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(null, null));
    }

    @Test
    void testEndsWithSensitiveCase() {
        assertTrue(IOCase.SENSITIVE.checkEndsWith("ABC", "BC"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith("ABC", "Bc"));
    }

    @Test
    void testEndsWithInsensitiveCase() {
        assertTrue(IOCase.INSENSITIVE.checkEndsWith("ABC", "BC"));
        assertTrue(IOCase.INSENSITIVE.checkEndsWith("ABC", "Bc"));
    }

    @Test
    void testEndsWithSystemCase() {
        assertTrue(IOCase.SYSTEM.checkEndsWith("ABC", "BC"));
        assertEquals(WINDOWS, IOCase.SYSTEM.checkEndsWith("ABC", "Bc"));
    }

    @Test
    void testEndsWithFunctionality() {
        assertTrue(IOCase.SENSITIVE.checkEndsWith("ABC", ""));
        assertFalse(IOCase.SENSITIVE.checkEndsWith("ABC", "A"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith("ABC", "AB"));
        assertTrue(IOCase.SENSITIVE.checkEndsWith("ABC", "ABC"));
        assertTrue(IOCase.SENSITIVE.checkEndsWith("ABC", "BC"));
        assertTrue(IOCase.SENSITIVE.checkEndsWith("ABC", "C"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith("ABC", "ABCD"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith("", "ABC"));
        assertTrue(IOCase.SENSITIVE.checkEndsWith("", ""));

        assertFalse(IOCase.SENSITIVE.checkEndsWith("ABC", null));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(null, "ABC"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(null, null));
    }

    @Test
    void testEqualsSensitiveCase() {
        assertTrue(IOCase.SENSITIVE.checkEquals("ABC", "ABC"));
        assertFalse(IOCase.SENSITIVE.checkEquals("ABC", "Abc"));
    }

    @Test
    void testEqualsInsensitiveCase() {
        assertTrue(IOCase.INSENSITIVE.checkEquals("ABC", "ABC"));
        assertTrue(IOCase.INSENSITIVE.checkEquals("ABC", "Abc"));
    }

    @Test
    void testEqualsSystemCase() {
        assertTrue(IOCase.SYSTEM.checkEquals("ABC", "ABC"));
        assertEquals(WINDOWS, IOCase.SYSTEM.checkEquals("ABC", "Abc"));
    }

    @Test
    void testEqualsFunctionality() {
        assertFalse(IOCase.SENSITIVE.checkEquals("ABC", ""));
        assertFalse(IOCase.SENSITIVE.checkEquals("ABC", "A"));
        assertFalse(IOCase.SENSITIVE.checkEquals("ABC", "AB"));
        assertTrue(IOCase.SENSITIVE.checkEquals("ABC", "ABC"));
        assertFalse(IOCase.SENSITIVE.checkEquals("ABC", "BC"));
        assertFalse(IOCase.SENSITIVE.checkEquals("ABC", "C"));
        assertFalse(IOCase.SENSITIVE.checkEquals("ABC", "ABCD"));
        assertFalse(IOCase.SENSITIVE.checkEquals("", "ABC"));
        assertTrue(IOCase.SENSITIVE.checkEquals("", ""));

        assertFalse(IOCase.SENSITIVE.checkEquals("ABC", null));
        assertFalse(IOCase.SENSITIVE.checkEquals(null, "ABC"));
        assertTrue(IOCase.SENSITIVE.checkEquals(null, null));
    }

    @Test
    void testIndexOfSensitiveCase() {
        assertEquals(1, IOCase.SENSITIVE.checkIndexOf("ABC", 0, "BC"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABC", 0, "Bc"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, "Bc"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, null));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABC", 0, null));
    }

    @Test
    void testIndexOfInsensitiveCase() {
        assertEquals(1, IOCase.INSENSITIVE.checkIndexOf("ABC", 0, "BC"));
        assertEquals(1, IOCase.INSENSITIVE.checkIndexOf("ABC", 0, "Bc"));
    }

    @Test
    void testIndexOfSystemCase() {
        assertEquals(1, IOCase.SYSTEM.checkIndexOf("ABC", 0, "BC"));
        assertEquals(WINDOWS ? 1 : -1, IOCase.SYSTEM.checkIndexOf("ABC", 0, "Bc"));
    }

    @Test
    void testIndexOfFunctionality() {
        // start
        assertEquals(0, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 0, "A"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 1, "A"));
        assertEquals(0, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 0, "AB"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 1, "AB"));
        assertEquals(0, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 0, "ABC"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 1, "ABC"));

        // middle
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 0, "D"));
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 3, "D"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 4, "D"));
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 0, "DE"));
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 3, "DE"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 4, "DE"));
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 0, "DEF"));
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 3, "DEF"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 4, "DEF"));

        // end
        assertEquals(9, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 0, "J"));
        assertEquals(9, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 8, "J"));
        assertEquals(9, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 9, "J"));
        assertEquals(8, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 0, "IJ"));
        assertEquals(8, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 8, "IJ"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 9, "IJ"));
        assertEquals(7, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 6, "HIJ"));
        assertEquals(7, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 7, "HIJ"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 8, "HIJ"));

        // not found
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABCDEFGHIJ", 0, "DED"));

        // too long
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("DEF", 0, "ABCDEFGHIJ"));

        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABC", 0, null));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, "ABC"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, null));
    }

    @Test
    void testRegionMatchesSensitiveCase() {
        assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "AB"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "Ab"));
    }

    @Test
    void testRegionMatchesInsensitiveCase() {
        assertTrue(IOCase.INSENSITIVE.checkRegionMatches("ABC", 0, "AB"));
        assertTrue(IOCase.INSENSITIVE.checkRegionMatches("ABC", 0, "Ab"));
    }

    @Test
    void testRegionMatchesSystemCase() {
        assertTrue(IOCase.SYSTEM.checkRegionMatches("ABC", 0, "AB"));
        assertEquals(WINDOWS, IOCase.SYSTEM.checkRegionMatches("ABC", 0, "Ab"));
    }

    @Test
    void testRegionMatchesFunctionality() {
        assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, ""));
        assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "A"));
        assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "AB"));
        assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "ABC"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "BC"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "C"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "ABCD"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("", 0, "ABC"));
        assertTrue(IOCase.SENSITIVE.checkRegionMatches("", 0, ""));

        assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 1, ""));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 1, "A"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 1, "AB"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 1, "ABC"));
        assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 1, "BC"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 1, "C"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 1, "ABCD"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("", 1, "ABC"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("", 1, ""));

        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, null));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, "ABC"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, null));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 1, null));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 1, "ABC"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 1, null));
    }

    @Test
    void testStartsWithSensitiveCase() {
        assertTrue(IOCase.SENSITIVE.checkStartsWith("ABC", "AB"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith("ABC", "Ab"));
    }

    @Test
    void testStartsWithInsensitiveCase() {
        assertTrue(IOCase.INSENSITIVE.checkStartsWith("ABC", "AB"));
        assertTrue(IOCase.INSENSITIVE.checkStartsWith("ABC", "Ab"));
    }

    @Test
    void testStartsWithSystemCase() {
        assertTrue(IOCase.SYSTEM.checkStartsWith("ABC", "AB"));
        assertEquals(WINDOWS, IOCase.SYSTEM.checkStartsWith("ABC", "Ab"));
    }

    @Test
    void testStartsWithFunctionality() {
        assertTrue(IOCase.SENSITIVE.checkStartsWith("ABC", ""));
        assertTrue(IOCase.SENSITIVE.checkStartsWith("ABC", "A"));
        assertTrue(IOCase.SENSITIVE.checkStartsWith("ABC", "AB"));
        assertTrue(IOCase.SENSITIVE.checkStartsWith("ABC", "ABC"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith("ABC", "BC"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith("ABC", "C"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith("ABC", "ABCD"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith("", "ABC"));
        assertTrue(IOCase.SENSITIVE.checkStartsWith("", ""));

        assertFalse(IOCase.SENSITIVE.checkStartsWith("ABC", null));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(null, "ABC"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(null, null));
    }

    @Test
    void testForName() {
        assertEquals(IOCase.SENSITIVE, IOCase.forName("Sensitive"));
        assertEquals(IOCase.INSENSITIVE, IOCase.forName("Insensitive"));
        assertEquals(IOCase.SYSTEM, IOCase.forName("System"));
        assertThrows(IllegalArgumentException.class, () -> IOCase.forName("Blah"));
        assertThrows(IllegalArgumentException.class, () -> IOCase.forName(null));
    }

    @Test
    void testGetName() {
        assertEquals("Sensitive", IOCase.SENSITIVE.getName());
        assertEquals("Insensitive", IOCase.INSENSITIVE.getName());
        assertEquals("System", IOCase.SYSTEM.getName());
    }

    @Test
    void testGetScratchByteArray() {
        final byte[] array = IOUtils.getScratchByteArray();
        assertArrayElementsAreZero(array);
        Arrays.fill(array, (byte) 1);
        assertArrayElementsAreZero(IOUtils.getScratchCharArray());
    }

    @Test
    void testGetScratchByteArrayWriteOnly() {
        final byte[] array = IOUtils.getScratchByteArrayWriteOnly();
        assertArrayElementsAreZero(array);
        Arrays.fill(array, (byte) 1);
        assertArrayElementsAreZero(IOUtils.getScratchCharArray());
    }

    @Test
    void testGetScratchCharArray() {
        final char[] array = IOUtils.getScratchCharArray();
        assertArrayElementsAreZero(array);
        Arrays.fill(array, (char) 1);
        assertArrayElementsAreZero(IOUtils.getScratchCharArray());
    }

    @Test
    void testGetScratchCharArrayWriteOnly() {
        final char[] array = IOUtils.getScratchCharArrayWriteOnly();
        assertArrayElementsAreZero(array);
        Arrays.fill(array, (char) 1);
        assertArrayElementsAreZero(IOUtils.getScratchCharArray());
    }

    @Test
    void testIsCaseSensitive() {
        assertTrue(IOCase.SENSITIVE.isCaseSensitive());
        assertFalse(IOCase.INSENSITIVE.isCaseSensitive());
        assertEquals(!WINDOWS, IOCase.SYSTEM.isCaseSensitive());
    }

    @Test
    void testIsCaseSensitiveStatic() {
        assertTrue(IOCase.isCaseSensitive(IOCase.SENSITIVE));
        assertFalse(IOCase.isCaseSensitive(IOCase.INSENSITIVE));
        assertEquals(!WINDOWS, IOCase.isCaseSensitive(IOCase.SYSTEM));
    }

    @Test
    void testSerialization() throws Exception {
        assertSame(IOCase.SENSITIVE, serializeIOCase(IOCase.SENSITIVE));
        assertSame(IOCase.INSENSITIVE, serializeIOCase(IOCase.INSENSITIVE));
        assertSame(IOCase.SYSTEM, serializeIOCase(IOCase.SYSTEM));
    }

    @Test
    void testToString() {
        assertEquals("Sensitive", IOCase.SENSITIVE.toString());
        assertEquals("Insensitive", IOCase.INSENSITIVE.toString());
        assertEquals("System", IOCase.SYSTEM.toString());
    }
}