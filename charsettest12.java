package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Contains tests for the hashCode() method of the {@link CharSet} class.
 * This class focuses on verifying the contract of the hashCode() method.
 */
class CharSetHashCodeTest extends AbstractLangTest {

    @Test
    @DisplayName("hashCode() should be consistent for equal CharSet instances")
    void hashCode_forEqualInstances_shouldBeEqual() {
        // The hashCode contract requires that if two objects are equal (via .equals()),
        // their hash codes must also be equal.
        // CharSet instances are considered equal if they are created from the same definition string.

        // Arrange: Create pairs of CharSet objects from identical definitions
        final CharSet setFromChars1 = CharSet.getInstance("abc");
        final CharSet setFromChars2 = CharSet.getInstance("abc");

        final CharSet setFromRange1 = CharSet.getInstance("a-c");
        final CharSet setFromRange2 = CharSet.getInstance("a-c");

        final CharSet setFromNegatedRange1 = CharSet.getInstance("^a-c");
        final CharSet setFromNegatedRange2 = CharSet.getInstance("^a-c");

        // Assert: The hash codes for each pair must be equal
        assertEquals(setFromChars1.hashCode(), setFromChars2.hashCode(),
            "Hash codes for identical 'abc' definitions should be equal");
        assertEquals(setFromRange1.hashCode(), setFromRange2.hashCode(),
            "Hash codes for identical 'a-c' range definitions should be equal");
        assertEquals(setFromNegatedRange1.hashCode(), setFromNegatedRange2.hashCode(),
            "Hash codes for identical '^a-c' negated range definitions should be equal");
    }

    @Test
    @DisplayName("hashCode() should differ for unequal CharSet instances")
    void hashCode_forUnequalInstances_shouldBeDifferent() {
        // According to the CharSet.equals() documentation, instances are unequal if their definitions differ,
        // even if they represent the same set of characters (e.g., "abc" vs "a-c").
        // While not strictly required by the hashCode contract, unequal objects should have different
        // hash codes to ensure good performance in hash-based collections.

        // Arrange: Create CharSet objects with different definitions
        final CharSet setByChars = CharSet.getInstance("abc");
        final CharSet setByRange = CharSet.getInstance("a-c");
        final CharSet setByNegation = CharSet.getInstance("^a-c");
        final CharSet anotherSet = CharSet.getInstance("xyz");

        // Assert: Hash codes for unequal objects should be different
        assertNotEquals(setByChars.hashCode(), setByRange.hashCode(),
            "Hash codes for 'abc' and 'a-c' should be different");
        assertNotEquals(setByChars.hashCode(), setByNegation.hashCode(),
            "Hash codes for 'abc' and '^a-c' should be different");
        assertNotEquals(setByRange.hashCode(), setByNegation.hashCode(),
            "Hash codes for 'a-c' and '^a-c' should be different");
        assertNotEquals(setByChars.hashCode(), anotherSet.hashCode(),
            "Hash codes for 'abc' and 'xyz' should be different");
    }
}