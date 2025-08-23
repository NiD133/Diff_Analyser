package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that {@link StringUtil#isBlank(String)} correctly identifies an empty string as blank.
     */
    @Test
    public void isBlankReturnsTrueForEmptyString() {
        // An empty string is a primary case for a "blank" string.
        // The isBlank() method should return true.
        assertTrue("An empty string should be considered blank.", StringUtil.isBlank(""));
    }
}