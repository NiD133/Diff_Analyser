package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link StringUtil#isNumeric(String)}.
 */
public class StringUtilIsNumericTest {

    @Test
    public void isNumericShouldReturnFalseForEmptyString() {
        // The isNumeric method should return false for an empty input string.
        boolean result = StringUtil.isNumeric("");
        assertFalse("An empty string should not be considered numeric", result);
    }
}