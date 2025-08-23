package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that countriesByLanguage() returns an empty list when provided with an
     * invalid or non-existent language code. The method is expected to handle
     * malformed input gracefully without throwing an exception.
     */
    @Test
    public void countriesByLanguageShouldReturnEmptyListForInvalidLanguageCode() {
        // Arrange: Define an input string that is clearly not a valid language code.
        final String invalidLanguageCode = "q@:a$7mI";

        // Act: Call the method under test.
        final List<Locale> countries = LocaleUtils.countriesByLanguage(invalidLanguageCode);

        // Assert: Verify that the result is an empty list, as expected.
        assertNotNull("The returned list should never be null.", countries);
        assertTrue("Expected an empty list for an invalid language code.", countries.isEmpty());
    }
}