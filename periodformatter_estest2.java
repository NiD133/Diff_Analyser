package org.joda.time.format;

import org.joda.time.PeriodType;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertSame;

/**
 * Tests for {@link PeriodFormatter}.
 * This class focuses on improving the understandability of an auto-generated test.
 */
public class PeriodFormatter_ESTestTest2 {

    /**
     * Tests that calling {@link PeriodFormatter#withParseType(PeriodType)} with the
     * same PeriodType returns the original formatter instance.
     * <p>
     * This is an optimization for immutable objects, where creating a new instance
     * is unnecessary if no properties are changed.
     */
    @Test
    public void withParseType_shouldReturnSameInstance_whenTypeIsUnchanged() {
        // Arrange
        // Create a formatter with a specific parse type. The printer and parser
        // are not relevant to this test and can be null.
        PeriodType initialParseType = PeriodType.millis();
        PeriodFormatter originalFormatter = new PeriodFormatter(null, null, Locale.KOREA, initialParseType);

        // Act
        // Call the method under test with the exact same PeriodType.
        PeriodFormatter newFormatter = originalFormatter.withParseType(initialParseType);

        // Assert
        // The formatter should return itself because it is immutable and no change was requested.
        assertSame("Expected the same formatter instance when the parse type is not changed",
                originalFormatter, newFormatter);
    }
}