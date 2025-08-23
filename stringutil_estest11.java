package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void isAsciiReturnsTrueForStringContainingOnlyAsciiCharacters() {
        // Arrange: Define a string that contains only characters within the standard ASCII set (0-127).
        String inputString = "{3 f\"nUAQw7TH,Y-";

        // Act: Call the method under test.
        boolean result = StringUtil.isAscii(inputString);

        // Assert: Verify that the method correctly identifies the string as ASCII.
        assertTrue("The string contains only ASCII characters and should be identified as such.", result);
    }
}