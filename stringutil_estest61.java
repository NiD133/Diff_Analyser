package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the StringUtil class.
 */
public class StringUtilTest {

    /**
     * Verifies that inSorted returns false when the search term ("needle")
     * is not present in the sorted array ("haystack").
     */
    @Test
    public void inSortedShouldReturnFalseWhenNeedleIsNotPresent() {
        // Arrange
        // A sorted array (haystack) containing a single whitespace string.
        String[] haystack = {"             "};
        // A string (needle) that is not in the haystack.
        String needle = "{3 f\"nUAQw7TH,Y-";

        // Act
        boolean isPresent = StringUtil.inSorted(needle, haystack);

        // Assert
        assertFalse("Expected the needle to not be found in the haystack.", isPresent);
    }
}