package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil#isActuallyWhitespace(int)} method.
 * This class focuses on a specific test case for the Form Feed character.
 */
public class StringUtilIsActuallyWhitespaceTest {

    @Test
    public void isActuallyWhitespace_shouldReturnTrue_forFormFeedCharacter() {
        // The original test used the integer 12, which is the ASCII code for the Form Feed character ('\f').
        // This test verifies that StringUtil correctly identifies Form Feed as a whitespace character.
        final int formFeedChar = '\f';

        assertTrue("The Form Feed character ('\\f') should be considered whitespace.", StringUtil.isActuallyWhitespace(formFeedChar));
    }
}