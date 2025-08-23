package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link StringUtil#in(String, String...)} method.
 */
public class StringUtilTest {

    /**
     * Verifies that StringUtil.in() returns false when the target string (the "needle")
     * is not present in the provided array of strings (the "haystack").
     */
    @Test
    public void in_whenNeedleIsNotInHaystack_returnsFalse() {
        // Arrange: Define the search array and the string to look for.
        String[] haystack = {"one", "two", "three"};
        String needle = "four"; // This string is not in the haystack.

        // Act: Call the method under test.
        boolean found = StringUtil.in(needle, haystack);

        // Assert: Verify that the result is false, as expected.
        assertFalse("Expected 'in' to return false for a non-existent string.", found);
    }
}