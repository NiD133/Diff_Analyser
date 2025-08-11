package org.apache.commons.io;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable and maintainable tests for IOCase.
 *
 * Key goals:
 * - Use meaningful test names and inputs.
 * - Avoid EvoSuite scaffolding and random strings.
 * - Assert clear, deterministic behavior per Javadoc.
 * - Keep SYSTEM assertions independent of the host OS by asserting properties.
 */
public class IOCaseTest {

    // --- Enum basics ---------------------------------------------------------

    @Test
    public void values_containsAllThreeConstants() {
        IOCase[] values = IOCase.values();
        assertEquals(3, values.length);
        assertArrayEquals(new IOCase[] { IOCase.SENSITIVE, IOCase.INSENSITIVE, IOCase.SYSTEM }, values);
    }

    @Test
    public void valueOf_returnsEnumConstants() {
        assertEquals(IOCase.SENSITIVE, IOCase.valueOf("SENSITIVE"));
        assertEquals(IOCase.INSENSITIVE, IOCase.valueOf("INSENSITIVE"));
        assertEquals(IOCase.SYSTEM, IOCase.valueOf("SYSTEM"));
    }

    @Test
    public void getName_and_toString_matchExpected() {
        assertEquals("Sensitive", IOCase.SENSITIVE.getName());
        assertEquals("Sensitive", IOCase.SENSITIVE.toString());

        assertEquals("Insensitive", IOCase.INSENSITIVE.getName());
        assertEquals("Insensitive", IOCase.INSENSITIVE.toString());

        assertEquals("System", IOCase.SYSTEM.getName());
        assertEquals("System", IOCase.SYSTEM.toString());
    }

    // --- forName -------------------------------------------------------------

    @Test
    public void forName_validNamesReturnConstants() {
        assertEquals(IOCase.SENSITIVE, IOCase.forName("Sensitive"));
        assertEquals(IOCase.INSENSITIVE, IOCase.forName("Insensitive"));
        assertEquals(IOCase.SYSTEM, IOCase.forName("System"));
    }

    @Test
    public void forName_invalidNameThrows() {
        try {
            IOCase.forName("NotAnIOCase");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Illegal IOCase name: NotAnIOCase"));
        }
    }

    // --- isCaseSensitive (static and instance) ------------------------------

    @Test
    public void isCaseSensitive_staticHandlesNull() {
        assertFalse(IOCase.isCaseSensitive(null));
        assertTrue(IOCase.isCaseSensitive(IOCase.SENSITIVE));
        assertFalse(IOCase.isCaseSensitive(IOCase.INSENSITIVE));
        assertEquals(IOCase.SYSTEM.isCaseSensitive(), IOCase.isCaseSensitive(IOCase.SYSTEM));
    }

    @Test
    public void system_isCaseSensitive_matchesFileSystem() {
        // Keep this deterministic across environments by comparing to FileSystem:
        assertEquals(FileSystem.getCurrent().isCaseSensitive(), IOCase.SYSTEM.isCaseSensitive());
    }

    // Property: SYSTEM equalsIgnoreCase behavior is the opposite of sensitivity.
    @Test
    public void system_equalsPropertyMatchesSensitivity() {
        boolean equalsIgnoringCase = IOCase.SYSTEM.checkEquals("a", "A");
        assertEquals(!IOCase.SYSTEM.isCaseSensitive(), equalsIgnoringCase);
    }

    // --- value(value, defaultValue) -----------------------------------------

    @Test
    public void value_returnsGivenValueWhenNonNull() {
        assertEquals(IOCase.SENSITIVE, IOCase.value(IOCase.SENSITIVE, IOCase.INSENSITIVE));
    }

    @Test
    public void value_returnsDefaultWhenValueIsNull() {
        assertEquals(IOCase.INSENSITIVE, IOCase.value(null, IOCase.INSENSITIVE));
        assertEquals(IOCase.SYSTEM, IOCase.value(null, IOCase.SYSTEM));
    }

    // --- checkEquals ---------------------------------------------------------

    @Test
    public void checkEquals_sensitive_isCaseSensitive() {
        assertTrue(IOCase.SENSITIVE.checkEquals("Readme.txt", "Readme.txt"));
        assertFalse(IOCase.SENSITIVE.checkEquals("Readme.txt", "README.TXT"));
    }

    @Test
    public void checkEquals_insensitive_ignoresCase() {
        assertTrue(IOCase.INSENSITIVE.checkEquals("Readme.txt", "README.TXT"));
    }

    @Test
    public void checkEquals_nullInputsReturnFalseWhenOnlyOneIsNull() {
        assertFalse(IOCase.SENSITIVE.checkEquals(null, "x"));
        assertFalse(IOCase.SENSITIVE.checkEquals("x", null));
        assertFalse(IOCase.INSENSITIVE.checkEquals(null, "x"));
        assertFalse(IOCase.INSENSITIVE.checkEquals("x", null));
    }

    // --- checkStartsWith -----------------------------------------------------

    @Test
    public void checkStartsWith_sensitive() {
        assertTrue(IOCase.SENSITIVE.checkStartsWith("Readme.txt", "Read"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith("Readme.txt", "read"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(null, "x"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith("x", null));
    }

    @Test
    public void checkStartsWith_insensitive() {
        assertTrue(IOCase.INSENSITIVE.checkStartsWith("Readme.txt", "read"));
    }

    // --- checkEndsWith -------------------------------------------------------

    @Test
    public void checkEndsWith_sensitive() {
        assertTrue(IOCase.SENSITIVE.checkEndsWith("Readme.TXT", ".TXT"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith("Readme.TXT", ".txt"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(null, "x"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith("x", null));
    }

    @Test
    public void checkEndsWith_insensitive() {
        assertTrue(IOCase.INSENSITIVE.checkEndsWith("Readme.TXT", ".txt"));
    }

    // --- checkIndexOf --------------------------------------------------------

    @Test
    public void checkIndexOf_basicSearch() {
        String s = "fileName.txt";
        assertEquals(4, IOCase.SENSITIVE.checkIndexOf(s, 0, "Name"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(s, 0, "name"));
        assertEquals(4, IOCase.INSENSITIVE.checkIndexOf(s, 0, "name"));
    }

    @Test
    public void checkIndexOf_negativeStartIndexIsClampedToZero() {
        assertEquals(0, IOCase.SENSITIVE.checkIndexOf("file", -5, "f"));
        assertEquals(0, IOCase.INSENSITIVE.checkIndexOf("FILE", -100, "f"));
    }

    @Test
    public void checkIndexOf_outOfRangeStartIndexOrNullSearch() {
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("file", 1000, "f"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("file", 0, null));
    }

    // --- checkRegionMatches --------------------------------------------------

    @Test
    public void checkRegionMatches_basic() {
        String s = "fileNAME.txt";
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(s, 4, "name"));
        assertTrue(IOCase.INSENSITIVE.checkRegionMatches(s, 4, "name"));
    }

    @Test
    public void checkRegionMatches_handlesInvalidInputs() {
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, "x"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("x", 0, null));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("x", -1, "x"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("x", 5, "x"));
    }

    // --- checkCompareTo ------------------------------------------------------

    @Test
    public void checkCompareTo_sensitive_matchesStringCompareTo() {
        String a = "Abc";
        String b = "abc";
        int expected = a.compareTo(b);
        assertEquals(expected, IOCase.SENSITIVE.checkCompareTo(a, b));
    }

    @Test
    public void checkCompareTo_insensitive_treatsCaseEqually() {
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("Abc", "abc"));
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("abc", "Abc"));
    }

    @Test(expected = NullPointerException.class)
    public void checkCompareTo_throwsOnNulls() {
        IOCase.SENSITIVE.checkCompareTo(null, null);
    }
}