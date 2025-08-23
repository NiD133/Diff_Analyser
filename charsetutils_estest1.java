package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CharSetUtils} class, focusing on the squeeze method.
 */
public class CharSetUtilsTest {

    /**
     * Tests that squeeze() does not change the input string when the character
     * set contains no characters present in the string.
     */
    @Test
    public void testSqueezeWithNonMatchingSetReturnsUnchangedString() {
        // Arrange
        final String originalString = "offset cannot be negative";
        final String[] characterSet = {"0-9"}; // A set of characters (digits) not in the original string.

        // Act
        final String result = CharSetUtils.squeeze(originalString, characterSet);

        // Assert
        assertEquals("The string should remain unchanged when the set has no matching characters.",
                originalString, result);
    }
}