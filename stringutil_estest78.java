package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void isNumericShouldReturnFalseForNonNumericString() {
        // Arrange: A string containing a mix of letters, symbols, and numbers.
        String nonNumericInput = "Yn)+vHQao!UlQ0jsv(O";

        // Act: Check if the string is numeric.
        boolean isNumeric = StringUtil.isNumeric(nonNumericInput);

        // Assert: The result should be false.
        assertFalse("A string containing non-digit characters should not be considered numeric.", isNumeric);
    }
}