package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link CharRange#contains(CharRange)}.
 */
// The original class name and inheritance are preserved to respect the existing test suite structure.
public class CharRange_ESTestTest4 extends CharRange_ESTest_scaffolding {

    /**
     * Tests that a negated CharRange (e.g., "all characters EXCEPT '*'") does not
     * contain a positive CharRange of the character it excludes (e.g., "'*'").
     */
    @Test
    public void contains_negatedRangeShouldNotContainTheExcludedRange() {
        // Arrange: Create two opposing character ranges for the same character.
        // The negated range represents every character except '*'.
        CharRange negatedRange = CharRange.isNot('*');
        // The positive range represents only the character '*'.
        CharRange positiveRange = CharRange.is('*');

        // Act: Check if the negated range contains the positive range.
        boolean isContained = negatedRange.contains(positiveRange);

        // Assert: The result must be false.
        assertFalse("A negated range should not contain the very range it negates.", isContained);
    }
}