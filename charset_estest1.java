package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Contains tests for the {@link CharSet#hashCode()} method.
 * This class focuses on verifying the hash code's consistency and its
 * contract with the {@link CharSet#equals(Object)} method.
 */
public class CharSetHashCodeTest {

    /**
     * Tests the fundamental contract of hashCode(): if two objects are equal
     * according to the equals() method, they must have the same hash code.
     * It also verifies that the hash code is consistent across multiple calls.
     */
    @Test
    public void testHashCodeContractForEqualObjects() {
        // Arrange: Create two CharSet objects with identical definitions.
        final CharSet set1 = CharSet.getInstance("a-z", "0-9");
        final CharSet set2 = CharSet.getInstance("a-z", "0-9");

        // Act & Assert
        // 1. Precondition: The objects must be equal for the hashCode contract to apply.
        assertEquals("CharSets with identical definitions should be equal.", set1, set2);

        // 2. The core assertion: Equal objects must have equal hash codes.
        assertEquals("Equal objects must have equal hash codes.", set1.hashCode(), set2.hashCode());

        // 3. Consistency check: The hash code should not change on subsequent calls.
        final int initialHash = set1.hashCode();
        assertEquals("hashCode() should be consistent across multiple calls.", initialHash, set1.hashCode());
    }

    /**
     * Tests that non-equal CharSet objects are likely to have different hash codes.
     * This test also covers the documented behavior that CharSets are compared based
     * on their definition, not just the characters they contain.
     */
    @Test
    public void testHashCodeForNonEqualObjects() {
        // Arrange: Create two CharSets with clearly different character sets.
        final CharSet alphaSet = CharSet.getInstance("a-z");
        final CharSet numericSet = CharSet.getInstance("0-9");

        // Assert: Non-equal objects should have different hash codes.
        // (While not a strict guarantee, it's a desirable property for a good hash function).
        assertNotEquals("CharSets with different definitions should not be equal.", alphaSet, numericSet);
        assertNotEquals("Hash codes for different sets should not be equal.", alphaSet.hashCode(), numericSet.hashCode());

        // Arrange: Create two CharSets that contain the same characters but are
        // defined differently. Per the Javadoc, these are NOT equal.
        final CharSet rangeSet = CharSet.getInstance("a-c");      // Defined as a range "a-c"
        final CharSet individualCharsSet = CharSet.getInstance("abc"); // Defined as individual characters "a", "b", "c"

        // Assert: Since the objects are not equal, their hash codes should also differ.
        assertNotEquals("Sets with different definitions are not equal, even if they contain the same characters.", rangeSet, individualCharsSet);
        assertNotEquals("Hash codes for sets with different definitions should not be equal.",
                        rangeSet.hashCode(), individualCharsSet.hashCode());
    }
}