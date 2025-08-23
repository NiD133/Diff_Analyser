package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void isNumericShouldReturnTrueForSingleDigitString() {
        // The isNumeric method should correctly identify a string containing a single digit.
        assertTrue("A string containing only the digit '3' should be identified as numeric.", StringUtil.isNumeric("3"));
    }
}