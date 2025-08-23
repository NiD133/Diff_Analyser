package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Locale;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that availableLocaleList() returns a non-empty list.
     * This is a basic sanity check to ensure the method is successfully retrieving
     * locales from the Java runtime environment.
     */
    @Test
    public void availableLocaleList_shouldReturnNonEmptyList() {
        // When: The list of available locales is retrieved.
        final List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        // Then: The list should contain at least one locale.
        assertFalse("The list of available locales should not be empty", availableLocales.isEmpty());
    }
}