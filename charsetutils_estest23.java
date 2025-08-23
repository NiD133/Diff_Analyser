package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that {@code CharSetUtils.containsAny()} returns false for an empty input string.
     *
     * <p>According to the Javadoc, {@code CharSetUtils.containsAny("", *)} should always
     * return false, regardless of the content of the character set. This test verifies
     * this behavior using a character set composed of nulls, which was the case in the
     * original auto-generated test.</p>
     */
    @Test
    public void testContainsAnyWithEmptyStringShouldReturnFalse() {
        // Arrange: An empty input string and a set of characters (in this case, nulls).
        final String input = "";
        final String[] characterSet = new String[7]; // An array of 7 nulls

        // Act: Call the method under test.
        final boolean result = CharSetUtils.containsAny(input, characterSet);

        // Assert: Verify that the result is false, as an empty string cannot contain any characters.
        assertFalse("An empty string should not contain any characters.", result);
    }
}