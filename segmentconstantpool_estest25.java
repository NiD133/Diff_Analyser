package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * This test suite focuses on the {@link SegmentConstantPool#regexMatches(String, String)} method,
 * ensuring its specialized regular expression handling works as expected.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that the regexMatches method correctly returns false when matching an empty string
     * against the pattern for constructor methods ("^<init>.*"). An empty string does not
     * start with "<init>", so a match should not be found.
     */
    @Test
    public void regexMatchesWithInitPatternShouldReturnFalseForEmptyString() {
        // Arrange: The "stupid" regex implementation in SegmentConstantPool is designed
        // to recognize a specific pattern for matching constructor names.
        final String initMethodPattern = SegmentConstantPool.REGEX_MATCH_INIT;
        final String emptyString = "";

        // Act: Attempt to match the empty string against the constructor pattern.
        final boolean isMatch = SegmentConstantPool.regexMatches(initMethodPattern, emptyString);

        // Assert: The result should be false, as the empty string does not match.
        assertFalse("An empty string should not match the '" + initMethodPattern + "' pattern", isMatch);
    }
}