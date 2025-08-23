package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that StringUtil.in() correctly returns true when the target string
     * is present in the provided array.
     */
    @Test
    public void inShouldReturnTrueWhenStringIsPresentInArray() {
        // Arrange: Define the array to be searched (the "haystack") and the
        // string to find (the "needle").
        String[] haystack = {"alpha", "beta", "", "gamma"};
        String needle = "";

        // Act: Call the method under test.
        boolean isFound = StringUtil.in(needle, haystack);

        // Assert: Verify that the string was found.
        assertTrue("Expected 'in' to return true for a string present in the array.", isFound);
    }
}