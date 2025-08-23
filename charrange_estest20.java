package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharRange}.
 */
public class CharRangeTest {

    /**
     * Tests that CharRange.equals() returns false when compared with an object of a different type.
     * This test also verifies that the CharRange object is correctly initialized.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedWithDifferentObjectType() {
        // Arrange: Create a CharRange and a plain Object to compare against.
        // Note: 'A' > '8', so the constructor should swap them to form the range ['8'.._.'A'].
        final CharRange charRange = CharRange.isIn('A', '8');
        final Object nonCharRangeObject = new Object();

        // Act: Call the equals method with the object of a different type.
        final boolean isEqual = charRange.equals(nonCharRangeObject);

        // Assert:
        // 1. Verify the primary behavior: equals() returns false for different types.
        assertFalse("equals() should return false for a non-CharRange object.", isEqual);

        // 2. Verify the state of the CharRange to ensure it was created correctly
        //    and was not mutated by the equals() call.
        assertEquals("Start of range should be the smaller character '8'", '8', charRange.getStart());
        assertEquals("End of range should be the larger character 'A'", 'A', charRange.getEnd());
    }
}