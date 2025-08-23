package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that isHexDigit() correctly returns false for a character
     * that is not a hexadecimal digit. This test uses a non-ASCII control
     * character to cover edge cases outside the typical '0'-'9', 'a'-'f' range.
     */
    @Test
    public void isHexDigit_shouldReturnFalse_forNonHexCharacter() {
        // Arrange: Define a character that is not a valid hexadecimal digit.
        // U+008F is a C1 control character, which falls outside the valid hex character set.
        char nonHexChar = '\u008F';

        // Act: Call the method under test.
        boolean result = StringUtil.isHexDigit(nonHexChar);

        // Assert: Verify that the method returned false.
        assertFalse("A non-hex character (U+008F) should not be identified as a hex digit.", result);
    }
}