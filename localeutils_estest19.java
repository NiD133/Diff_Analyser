package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that toLocale() throws an IllegalArgumentException for a string
     * with a valid language code followed by multiple trailing separators.
     */
    @Test
    public void toLocaleShouldThrowExceptionForMalformedStringWithTrailingSeparators() {
        // Arrange: Define an input string that is not a valid locale format.
        // The format "at--" has a language code but empty/invalid country and variant parts.
        final String malformedLocaleString = "at--";

        try {
            // Act: Attempt to convert the malformed string to a Locale.
            LocaleUtils.toLocale(malformedLocaleString);
            fail("Expected an IllegalArgumentException to be thrown for input: " + malformedLocaleString);
        } catch (final IllegalArgumentException e) {
            // Assert: Verify that the correct exception was thrown with a descriptive message.
            final String expectedMessage = "Invalid locale format: " + malformedLocaleString;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}