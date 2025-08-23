package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CharSetTestTest9 extends AbstractLangTest {

    @Test
    @DisplayName("Tests the contract of the CharSet.equals() method")
    void testEqualsContract() {
        // Arrange: Create various CharSet instances to test against each other.
        // 'setByEnumeration' and 'setByRange' represent the same characters ('a', 'b', 'c')
        // but are defined differently, which is key to testing the equals() method.
        final CharSet setByEnumeration = CharSet.getInstance("abc");
        final CharSet setByEnumerationAgain = CharSet.getInstance("abc");

        final CharSet setByRange = CharSet.getInstance("a-c");
        final CharSet setByRangeAgain = CharSet.getInstance("a-c");

        final CharSet setNegated = CharSet.getInstance("^a-c");
        final CharSet setNegatedAgain = CharSet.getInstance("^a-c");

        // --- Assertions for the equals() contract ---

        // 1. Non-nullity: An object must not be equal to null.
        assertNotEquals(null, setByEnumeration);

        // 2. Reflexivity: An object must be equal to itself.
        assertEquals(setByEnumeration, setByEnumeration);
        assertEquals(setByRange, setByRange);
        assertEquals(setNegated, setNegated);

        // 3. Equality & Symmetry: Instances from the same definition string must be equal.
        assertEquals(setByEnumeration, setByEnumerationAgain);
        assertEquals(setByEnumerationAgain, setByEnumeration); // Check symmetry

        assertEquals(setByRange, setByRangeAgain);
        assertEquals(setByRangeAgain, setByRange); // Check symmetry

        assertEquals(setNegated, setNegatedAgain);
        assertEquals(setNegatedAgain, setNegated); // Check symmetry

        // 4. Inequality & Symmetry: Instances from different definitions must NOT be equal.
        // This is true even if they represent the same set of characters.
        final String reason = "CharSet.equals() compares definitions, not just the characters in the set.";
        assertNotEquals(setByEnumeration, setByRange, reason);
        assertNotEquals(setByRange, setByEnumeration, reason); // Check symmetry

        // Also, a set must not be equal to a negated set.
        assertNotEquals(setByEnumeration, setNegated);
        assertNotEquals(setNegated, setByEnumeration); // Check symmetry

        assertNotEquals(setByRange, setNegated);
        assertNotEquals(setNegated, setByRange); // Check symmetry
    }
}