package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    @Test
    public void isLanguageUndetermined_shouldReturnFalse_forLocaleWithDeterminedLanguage() {
        // Arrange: Create a locale with a well-defined language ("de").
        final Locale germanLocale = Locale.GERMANY;

        // Act: Check if the locale's language is undetermined.
        final boolean isUndetermined = LocaleUtils.isLanguageUndetermined(germanLocale);

        // Assert: The result should be false, as the language is clearly defined.
        assertFalse("Locale.GERMANY has a defined language ('de') and should not be considered undetermined.", isUndetermined);
    }
}