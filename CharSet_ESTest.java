package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for CharSet.
 *
 * Notes:
 * - Tests stay in the same package to access package-private APIs like getCharRanges().
 * - Avoids EvoSuite runner/dependencies and uses clear, intention-revealing names.
 */
public class CharSetTest {

    // getInstance

    @Test
    public void getInstance_withNullVarargs_returnsNull() {
        assertNull(CharSet.getInstance((String[]) null));
    }

    @Test
    public void getInstance_withSingleRange_returnsNonNull() {
        assertNotNull(CharSet.getInstance("a-z"));
    }

    @Test
    public void getInstance_withArbitraryDefinition_returnsNonNull() {
        assertNotNull(CharSet.getInstance("]ZOr9"));
    }

    // Constructor

    @Test(expected = NullPointerException.class)
    public void constructor_withNullArray_throwsNPE() {
        new CharSet((String[]) null);
    }

    @Test
    public void constructor_withOnlyNullElements_yieldsEmptyRanges() {
        String[] defs = new String[3]; // all nulls
        CharSet set = new CharSet(defs);

        CharRange[] ranges = set.getCharRanges();
        assertEquals(0, ranges.length);
    }

    // contains

    @Test
    public void contains_withUppercaseRange_returnsTrueForT() {
        CharSet set = CharSet.getInstance("A-Z");
        assertTrue(set.contains('T'));
    }

    @Test
    public void contains_withNoDefinitions_returnsFalse() {
        // Using an array with only null elements results in an empty set.
        String[] defs = new String[2]; // all nulls
        CharSet set = CharSet.getInstance(defs);

        assertFalse(set.contains('T'));
    }

    // equals and hashCode

    @Test
    public void equals_isReflexive() {
        assertTrue(CharSet.ASCII_ALPHA_UPPER.equals(CharSet.ASCII_ALPHA_UPPER));
    }

    @Test
    public void equals_returnsFalseWhenComparedToDifferentType() {
        CharSet set = CharSet.getInstance("a-z");
        assertFalse(set.equals(new Object()));
    }

    @Test
    public void equals_returnsTrueForSeparateInstancesCreatedFromSameInput() {
        String[] defs = new String[9]; // all nulls -> both become empty sets
        CharSet set1 = CharSet.getInstance(defs);
        CharSet set2 = CharSet.getInstance(defs);

        assertNotSame(set1, set2); // ensure not the same reference
        assertTrue(set1.equals(set2));
        assertEquals(set1.hashCode(), set2.hashCode());
    }

    @Test
    public void equals_definitionMatters_equivalentCharsDifferentSyntaxAreNotEqual() {
        CharSet listSyntax = CharSet.getInstance("abc");
        CharSet rangeSyntax = CharSet.getInstance("a-c");
        assertFalse(listSyntax.equals(rangeSyntax));
    }

    @Test
    public void hashCode_doesNotThrowForArbitraryDefinition() {
        CharSet set = CharSet.getInstance("a-zA-Z0-9");
        // Just ensure we can call it without exception.
        set.hashCode();
    }

    // getCharRanges

    @Test
    public void getCharRanges_onEmptyConstant_returnsEmptyArray() {
        CharRange[] ranges = CharSet.EMPTY.getCharRanges();
        assertEquals(0, ranges.length);
    }

    // toString

    @Test
    public void toString_onEmptySet_returnsSquareBrackets() {
        String[] defs = new String[1]; // [null] -> empty set
        CharSet set = CharSet.getInstance(defs);
        assertEquals("[]", set.toString());
    }
}