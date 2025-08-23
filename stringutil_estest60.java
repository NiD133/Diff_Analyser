package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for StringUtil.
 */
public class StringUtilTest {

    @Test
    public void inSortedShouldReturnTrueWhenStringIsPresentInSingleElementArray() {
        // Arrange: Define the search term (needle) and the sorted array (haystack).
        // This is the simplest case where the array contains only the needle.
        String needle = "a";
        String[] haystack = {"a"};

        // Act: Call the method under test.
        boolean found = StringUtil.inSorted(needle, haystack);

        // Assert: Verify that the string was found.
        assertTrue("Expected the string to be found in the sorted array.", found);
    }
}