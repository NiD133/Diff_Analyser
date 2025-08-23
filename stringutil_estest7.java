package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} utility class.
 */
public class StringUtilTest {

    /**
     * Verifies that the isAsciiLetter() method correctly identifies a lowercase
     * ASCII letter as a valid letter.
     */
    @Test
    public void isAsciiLetter_withLowerCaseLetter_returnsTrue() {
        // Arrange: Define a lowercase ASCII character to test.
        char lowerCaseLetter = 'z';

        // Act: Call the method under test.
        boolean result = StringUtil.isAsciiLetter(lowerCaseLetter);

        // Assert: Verify that the method returns true.
        assertTrue("The character 'z' should be correctly identified as an ASCII letter.", result);
    }
}