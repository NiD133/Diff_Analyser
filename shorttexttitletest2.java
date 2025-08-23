package org.jfree.chart.title;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
class ShortTextTitleTest {

    @Test
    @DisplayName("hashCode() returns the same value for two equal objects")
    void hashCode_whenObjectsAreEqual_thenHashCodesShouldBeEqual() {
        // Given two ShortTextTitle objects that are considered equal
        ShortTextTitle title1 = new ShortTextTitle("ABC");
        ShortTextTitle title2 = new ShortTextTitle("ABC");

        // A quick check to ensure the equals() method works as expected,
        // which is a precondition for the hashCode() contract.
        assertEquals(title1, title2, "Precondition failed: two titles with the same text should be equal.");

        // When their hash codes are calculated
        int hashCode1 = title1.hashCode();
        int hashCode2 = title2.hashCode();

        // Then the hash codes must be equal
        assertEquals(hashCode1, hashCode2, "The hashCode() contract is broken: equal objects must have equal hash codes.");
    }
}