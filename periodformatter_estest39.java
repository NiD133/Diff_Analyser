package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Locale;

/**
 * Tests for the immutability of the {@link PeriodFormatter} class, specifically
 * focusing on the {@code withLocale()} method.
 */
public class PeriodFormatter_ESTestTest39 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that calling withLocale() on a PeriodFormatter returns a new instance
     * with the updated locale, rather than modifying the original instance.
     */
    @Test
    public void withLocale_shouldReturnNewInstance_whenLocaleIsChanged() {
        // Arrange: Create an initial formatter with the default (null) locale.
        // A dummy printer/parser is required for the constructor.
        PeriodFormatterBuilder.Literal dummyPrinterParser = new PeriodFormatterBuilder.Literal("dummy");
        PeriodFormatter initialFormatter = new PeriodFormatter(dummyPrinterParser, dummyPrinterParser);

        // Verify the initial state
        assertNull("The initial formatter should have a null locale.", initialFormatter.getLocale());

        // Act: Create a new formatter by applying a specific locale.
        Locale koreanLocale = Locale.KOREAN;
        PeriodFormatter formatterWithKoreanLocale = initialFormatter.withLocale(koreanLocale);

        // Assert: The method should return a new instance with the correct locale.
        assertNotSame("withLocale should return a new instance for a different locale.",
                      initialFormatter, formatterWithKoreanLocale);
        assertEquals("The new formatter should have the Korean locale set.",
                     koreanLocale, formatterWithKoreanLocale.getLocale());
    }
}