package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

/**
 * Tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that countriesByLanguage() returns a non-null, empty list
     * when the input language code is null, as per its Javadoc contract.
     */
    @Test
    public void countriesByLanguageShouldReturnEmptyListForNullInput() {
        // Arrange: The input is a null language code.
        final String languageCode = null;

        // Act: Call the method under test.
        final List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);

        // Assert: Verify the behavior is correct.
        assertNotNull("The returned list should never be null.", countries);
        assertTrue("Expected an empty list for a null language code.", countries.isEmpty());
    }
}