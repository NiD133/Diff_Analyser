package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void isHexDigitShouldReturnTrueForUppercaseHexCharacter() {
        // The character 'A' is a valid uppercase hexadecimal digit.
        assertTrue("Character 'A' should be recognized as a hex digit", StringUtil.isHexDigit('A'));
    }
}