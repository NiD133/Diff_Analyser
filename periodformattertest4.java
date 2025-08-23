package org.joda.time.format;

import java.util.Locale;
import junit.framework.TestCase;

/**
 * Tests the locale-related functionality of PeriodFormatter.
 */
public class PeriodFormatterLocaleTest extends TestCase {

    private static final PeriodFormatter BASE_FORMATTER = ISOPeriodFormat.standard();

    public void testGetLocale_isInitiallyNull() {
        // A default formatter should not have a specific locale.
        assertNull(BASE_FORMATTER.getLocale());
    }

    public void testWithLocale_returnsNewFormatterWithSpecifiedLocale() {
        // Arrange
        assertNull("Pre-condition: base formatter should have no locale", BASE_FORMATTER.getLocale());

        // Act
        PeriodFormatter frenchFormatter = BASE_FORMATTER.withLocale(Locale.FRENCH);

        // Assert
        assertEquals(Locale.FRENCH, frenchFormatter.getLocale());
    }

    public void testWithLocale_doesNotMutateOriginalFormatter() {
        // Arrange
        PeriodFormatter originalFormatter = ISOPeriodFormat.standard();

        // Act: The result of withLocale is intentionally ignored.
        originalFormatter.withLocale(Locale.FRENCH);

        // Assert: The original formatter remains unchanged, proving immutability.
        assertNull(originalFormatter.getLocale());
    }

    public void testWithLocale_returnsSameInstanceIfLocaleIsUnchanged() {
        // Arrange
        PeriodFormatter frenchFormatter = BASE_FORMATTER.withLocale(Locale.FRENCH);
        PeriodFormatter nullLocaleFormatter = ISOPeriodFormat.standard();

        // Act & Assert: Calling withLocale with the same locale should return the same instance.
        assertSame("Should return same instance for the same non-null locale",
                frenchFormatter, frenchFormatter.withLocale(Locale.FRENCH));
        
        assertSame("Should return same instance for a null locale if already null",
                nullLocaleFormatter, nullLocaleFormatter.withLocale(null));
    }

    public void testWithLocale_canBeSetToNullToClearLocale() {
        // Arrange
        PeriodFormatter frenchFormatter = BASE_FORMATTER.withLocale(Locale.FRENCH);
        assertEquals("Pre-condition: formatter should have French locale", Locale.FRENCH, frenchFormatter.getLocale());

        // Act
        PeriodFormatter clearedFormatter = frenchFormatter.withLocale(null);

        // Assert
        assertNull("Locale should be cleared to null", clearedFormatter.getLocale());
    }
}