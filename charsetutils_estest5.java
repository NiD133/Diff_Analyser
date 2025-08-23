package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the squeeze method in {@link CharSetUtils}.
 */
public class CharSetUtilsSqueezeTest {

    /**
     * Tests that squeeze() does not modify a string when the repeated characters
     * are not part of the specified character set. This also verifies that a
     * null element within the set array is handled gracefully.
     */
    @Test
    public void testSqueezeShouldNotAlterStringWhenRepeatedCharIsNotInSet() {
        // Arrange: An input string with a repeated character ('.')
        final String input = "...";

        // A set of characters to squeeze that does NOT contain '.'
        // Including a null element to ensure it's handled correctly.
        final String[] squeezeSet = {"a-z", null, "A-Z"};

        // Act: Attempt to squeeze the string with the given set.
        final String result = CharSetUtils.squeeze(input, squeezeSet);

        // Assert: The string should remain unchanged because the repeated character '.'
        // is not in the specified set.
        assertEquals(input, result);
    }
}