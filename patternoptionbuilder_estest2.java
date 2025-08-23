package org.apache.commons.cli;

import org.junit.Test;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that a NullPointerException is thrown when a null string is passed
     * to the parsePattern method, as the pattern must not be null.
     */
    @Test(expected = NullPointerException.class)
    public void parsePatternShouldThrowNullPointerExceptionForNullInput() {
        PatternOptionBuilder.parsePattern(null);
    }
}