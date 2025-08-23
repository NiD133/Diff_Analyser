package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link LocaleUtils#toLocale(Locale)}.
 */
public class LocaleUtilsTest {

    @Test
    public void toLocale_withNonNullLocale_shouldReturnSameLocaleInstance() {
        // Arrange: Define a non-null input locale.
        final Locale inputLocale = Locale.ITALIAN;

        // Act: Call the method under test.
        final Locale resultLocale = LocaleUtils.toLocale(inputLocale);

        // Assert: The method should return the exact same instance that was passed in.
        assertSame("Expected the same Locale instance to be returned", inputLocale, resultLocale);
    }
}