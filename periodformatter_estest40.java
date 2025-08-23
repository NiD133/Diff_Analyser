package org.joda.time.format;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link PeriodFormatter}.
 */
public class PeriodFormatter_ESTestTest40 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that withLocale() returns a new formatter instance, confirming the class's immutability.
     */
    @Test
    public void withLocale_shouldReturnNewFormatterInstance() {
        // Arrange: Create a formatter with a specific locale.
        PeriodFormatterBuilder.Literal emptyLiteral = PeriodFormatterBuilder.Literal.EMPTY;
        Locale initialLocale = Locale.JAPANESE;
        PeriodFormatter initialFormatter = new PeriodFormatter(emptyLiteral, emptyLiteral, initialLocale, null);

        // Act: Create a new formatter by changing the locale.
        PeriodFormatter updatedFormatter = initialFormatter.withLocale(null);

        // Assert: The new formatter should be a different instance, and its locale should be updated,
        // while the original formatter remains unchanged.
        assertNotSame("A new formatter instance should be returned.", initialFormatter, updatedFormatter);
        assertNull("The new formatter's locale should be updated to null.", updatedFormatter.getLocale());
        assertEquals("The original formatter's locale should remain unchanged.", initialLocale, initialFormatter.getLocale());
    }
}