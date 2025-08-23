package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Locale;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that toLocale(Locale) returns the system's default Locale when the input is null.
     */
    @Test
    public void toLocale_withNullLocale_shouldReturnDefaultLocale() {
        // Arrange
        // The method's contract states it should return the default locale for a null input.
        final Locale expectedLocale = Locale.getDefault();

        // Act
        final Locale actualLocale = LocaleUtils.toLocale((Locale) null);

        // Assert
        assertEquals(expectedLocale, actualLocale);
    }
}