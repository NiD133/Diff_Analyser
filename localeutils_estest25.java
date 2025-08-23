package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that localeLookupList does not add a duplicate locale if the
     * primary locale and the default locale are the same.
     */
    @Test
    public void testLocaleLookupListWithIdenticalLocaleAndDefaultLocaleReturnsSingletonList() {
        // The documentation for localeLookupList states:
        // "The list will never contain the same locale twice."
        // This test verifies this behavior.

        // Arrange
        final Locale englishLocale = Locale.ENGLISH;

        // Act
        final List<Locale> lookupList = LocaleUtils.localeLookupList(englishLocale, englishLocale);

        // Assert
        // The lookup for "en" should produce ["en"]. Since the default locale is also "en",
        // it should not be added again. The final list must contain only one element.
        final List<Locale> expectedList = Collections.singletonList(englishLocale);
        assertEquals(expectedList, lookupList);
    }
}