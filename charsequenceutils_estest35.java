package org.apache.commons.lang3;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that regionMatches correctly compares identical sub-regions from
     * different CharSequence implementations (a String and a StringBuilder).
     */
    @Test
    public void testRegionMatchesWithEqualRegionsInStringAndStringBuilder() {
        // Arrange
        final String text = "The quick brown fox";
        final CharSequence sequence1 = text;
        final CharSequence sequence2 = new StringBuilder(text);

        final int offset = 4; // The start of the word "quick"
        final int length = 5; // The length of the word "quick"
        final boolean ignoreCase = false;

        // Act
        final boolean regionsMatch = CharSequenceUtils.regionMatches(sequence1, ignoreCase, offset, sequence2, offset, length);

        // Assert
        assertTrue("Expected regions to match for identical substrings in a String and StringBuilder.", regionsMatch);
    }
}