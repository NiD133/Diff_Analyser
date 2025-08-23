package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * This test class focuses on the {@link SegmentConstantPool#regexMatches} method.
 * Note: The original class name and structure suggest it was auto-generated. For better
 * maintainability, this test could be consolidated with other related tests for SegmentConstantPool.
 */
public class SegmentConstantPool_ESTestTest19 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that {@link SegmentConstantPool#regexMatches(String, String)} returns false
     * for a string that does not match the special-cased init-method pattern.
     *
     * The method under test uses a simplified, non-regex matching logic for performance,
     * and this test ensures it correctly handles non-matching cases for the init-pattern.
     */
    @Test
    public void regexMatches_InitPatternWithNonMatchingString_ShouldReturnFalse() {
        // Arrange: Use the predefined constant for the init-method pattern.
        final String initMethodPattern = SegmentConstantPool.REGEX_MATCH_INIT;
        final String nonMatchingString = "aRegularMethodName";

        // Act: Call the method with a string that clearly does not start with "<init>".
        final boolean isMatch = SegmentConstantPool.regexMatches(initMethodPattern, nonMatchingString);

        // Assert: The result should be false.
        assertFalse(
            "A string that does not start with '<init>' should not match the init-method pattern.",
            isMatch
        );
    }
}