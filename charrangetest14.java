package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the hashCode() method for the {@link CharRange} class.
 */
class CharRangeHashCodeTest {

    @Test
    @DisplayName("hashCode() should be consistent with the equals() contract")
    void hashCodeShouldBeConsistentWithEquals() {
        // Arrange: Create different CharRange instances for comparison.
        final CharRange rangeA = CharRange.is('a');
        final CharRange rangeAtoE = CharRange.isIn('a', 'e');
        final CharRange rangeBtoF = CharRange.isIn('b', 'f');

        // --- Assert: Equal objects must have equal hash codes ---

        // A new, but logically equal, object should have the same hash code.
        assertEquals(rangeA.hashCode(), CharRange.is('a').hashCode(),
            "Hash code should be same for equal single-char ranges");
        assertEquals(rangeAtoE.hashCode(), CharRange.isIn('a', 'e').hashCode(),
            "Hash code should be same for equal multi-char ranges");
        assertEquals(rangeBtoF.hashCode(), CharRange.isIn('b', 'f').hashCode(),
            "Hash code should be same for equal multi-char ranges");

        // --- Assert: Unequal objects should have unequal hash codes ---
        // Note: This is not a strict requirement of the hashCode contract,
        // but it is highly desirable for performance in hash-based collections.

        assertNotEquals(rangeA.hashCode(), rangeAtoE.hashCode(),
            "Hash code for 'a' and 'a-e' should be different");
        assertNotEquals(rangeA.hashCode(), rangeBtoF.hashCode(),
            "Hash code for 'a' and 'b-f' should be different");
        assertNotEquals(rangeAtoE.hashCode(), rangeBtoF.hashCode(),
            "Hash code for 'a-e' and 'b-f' should be different");
    }
}