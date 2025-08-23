package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link StringUtil}.
 */
public class StringUtilTest {

    @Test
    public void isNumericShouldReturnFalseForNullInput() {
        // The isNumeric method should handle null input gracefully by returning false.
        boolean result = StringUtil.isNumeric(null);
        assertFalse("isNumeric(null) should return false", result);
    }
}