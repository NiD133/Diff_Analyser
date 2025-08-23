package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

/**
 * Unit tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that localeLookupList() returns an empty list when the primary locale is null,
     * as specified by the method's contract.
     */
    @Test
    public void localeLookupList_shouldReturnEmptyList_whenLocaleIsNull() {
        // Arrange: Define null inputs for both the primary and default locales.
        final Locale nullLocale = null;
        final Locale nullDefaultLocale = null;

        // Act: Call the method under test.
        final List<Locale> localeList = LocaleUtils.localeLookupList(nullLocale, nullDefaultLocale);

        // Assert: Verify that the returned list is empty.
        assertTrue("The list should be empty when the primary locale is null.", localeList.isEmpty());
    }
}