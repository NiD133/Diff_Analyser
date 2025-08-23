package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test case verifies the properties of the predefined `CharSet.EMPTY` constant.
 * The original test was automatically generated and contained confusing, irrelevant code.
 */
public class CharSet_ESTestTest14 {

    /**
     * Tests that the `getCharRanges()` method returns an empty array for the
     * `CharSet.EMPTY` constant. This confirms that an empty set correctly
     * contains no character ranges.
     */
    @Test
    public void testGetCharlRanges_onEmptySet_returnsEmptyArray() {
        // Arrange: The test focuses on the predefined EMPTY CharSet constant.
        // No specific setup is needed.

        // Act: Retrieve the character ranges from the EMPTY CharSet.
        CharRange[] charRanges = CharSet.EMPTY.getCharRanges();

        // Assert: The returned array of character ranges should be empty.
        assertEquals("The CharSet.EMPTY constant should not contain any character ranges.", 0, charRanges.length);
    }
}