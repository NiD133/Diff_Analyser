package org.joda.time.format;

import org.junit.Test;
import java.util.Locale;
import org.joda.time.PeriodType;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link PeriodFormatter}.
 */
public class PeriodFormatterTest {

    /**
     * Tests that calling withLocale() with the same locale returns the same
     * PeriodFormatter instance, confirming an immutability optimization.
     */
    @Test
    public void withLocale_whenLocaleIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Locale chineseLocale = Locale.SIMPLIFIED_CHINESE;
        PeriodType periodType = PeriodType.minutes();
        PeriodFormatter originalFormatter = new PeriodFormatter(null, null, chineseLocale, periodType);

        // Act
        PeriodFormatter newFormatter = originalFormatter.withLocale(chineseLocale);

        // Assert
        // Since PeriodFormatter is immutable, calling a "with" method with the same value
        // should be optimized to return the original instance.
        assertSame("Expected the same instance when withLocale() is called with the same locale",
                originalFormatter, newFormatter);
    }
}