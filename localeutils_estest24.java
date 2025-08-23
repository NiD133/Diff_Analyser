package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that localeLookupList correctly generates a list for a language-only locale,
     * appending the specified default locale.
     */
    @Test
    public void localeLookupList_withLanguageOnlyLocale_shouldIncludeDefaultLocale() {
        // Arrange
        final Locale baseLocale = Locale.ENGLISH; // "en"
        final Locale defaultLocale = Locale.ROOT; // "" (empty language, country, variant)
        final List<Locale> expectedList = Arrays.asList(baseLocale, defaultLocale);

        // Act
        final List<Locale> actualList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);

        // Assert
        // The method should return a list containing the base locale followed by the default.
        assertEquals(expectedList, actualList);
    }
}