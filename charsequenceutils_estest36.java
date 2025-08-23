package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    @Test
    public void testRegionMatchesShouldReturnFalseForDifferentCharactersCaseSensitive() {
        // Arrange
        // Define two character sequences. We will compare a region from each.
        // The test will compare the character at index 1 of cs1 (',') 
        // with the character at index 0 of cs2 ('\'').
        final CharSequence cs1 = new StringBuilder("', text1");
        final CharSequence cs2 = "' text2";

        final boolean ignoreCase = false;
        final int startOffset1 = 1; // Start index in cs1, which is ','
        final int startOffset2 = 0; // Start index in cs2, which is '\''
        final int length = 1;       // We are comparing a region of one character

        // Act
        // Perform a case-sensitive region match. Since ',' and '\'' are different,
        // the result should be false.
        final boolean isMatch = CharSequenceUtils.regionMatches(cs1, ignoreCase, startOffset1, cs2, startOffset2, length);

        // Assert
        assertFalse("Expected regionMatches to return false for different characters in a case-sensitive comparison.", isMatch);
    }
}