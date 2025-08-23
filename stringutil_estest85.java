package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil#isBlank(String)} method.
 */
public class StringUtilTest {

    /**
     * Verifies that isBlank() returns true for a string that contains only space characters.
     */
    @Test
    public void isBlankReturnsTrueForStringContainingOnlySpaces() {
        // The isBlank method should identify a string composed of only spaces as "blank".
        assertTrue("A string containing only spaces should be considered blank", 
                   StringUtil.isBlank("                    "));
    }
}