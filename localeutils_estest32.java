package org.apache.commons.lang3;

import static org.junit.Assert.assertTrue;

import java.util.Locale;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that isLanguageUndetermined() returns true for a null Locale input.
     * The method is designed to handle nulls gracefully without throwing an exception.
     */
    @Test
    public void isLanguageUndeterminedShouldReturnTrueForNullLocale() {
        // Arrange: The class contract states that null input should be handled gracefully.
        // For this method, a null locale is considered to have an undetermined language.
        final Locale nullLocale = null;

        // Act: Call the method with the null locale.
        final boolean isUndetermined = LocaleUtils.isLanguageUndetermined(nullLocale);

        // Assert: Verify that the result is true as expected.
        assertTrue("A null locale should be treated as having an undetermined language.", isUndetermined);
    }
}