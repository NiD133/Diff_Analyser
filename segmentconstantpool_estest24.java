package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the static utility methods in {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that the custom regexMatches method correctly identifies the "match all"
     * pattern (".*") and returns true for any given input string. The source code
     * documentation for regexMatches explicitly states it's a simplified implementation
     * that only handles a few specific patterns, including this one.
     */
    @Test
    public void regexMatchesWithMatchAllPatternShouldAlwaysReturnTrue() {
        // Arrange: Use the "match all" pattern and an arbitrary string to test against.
        final String matchAllPattern = SegmentConstantPool.REGEX_MATCH_ALL;
        final String anyString = "this could be any string";

        // Act: Call the method under test.
        boolean result = SegmentConstantPool.regexMatches(matchAllPattern, anyString);

        // Assert: The result should always be true for the "match all" pattern.
        assertTrue("The '.*' pattern should match any string", result);
    }
}