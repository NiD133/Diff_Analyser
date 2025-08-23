package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void isDigitShouldReturnFalseForNonDigitCharacter() {
        // The isDigit method should correctly identify an alphabetical character as a non-digit.
        assertFalse("An alphabetical character like 'Y' should not be considered a digit.", StringUtil.isDigit('Y'));
    }

    @Test
    public void isDigitShouldReturnTrueForDigitCharacter() {
        // The isDigit method should correctly identify a numeric character as a digit.
        assertTrue("A numeric character like '5' should be considered a digit.", StringUtil.isDigit('5'));
    }
}