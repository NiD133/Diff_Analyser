package org.joda.time.convert;

import org.joda.time.PeriodType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link StringConverter}.
 * This test focuses on the getPeriodType method.
 */
public class StringConverterTest {

    private final StringConverter converter = StringConverter.INSTANCE;

    @Test
    public void getPeriodType_shouldReturnStandardType_forIsoPeriodString() {
        // Arrange: An ISO 8601 standard representation of a period.
        // "P2Y6M9D" means a period of 2 years, 6 months, and 9 days.
        String isoPeriodString = "P2Y6M9D";
        PeriodType expectedPeriodType = PeriodType.standard();

        // Act: Get the period type from the string.
        PeriodType actualPeriodType = converter.getPeriodType(isoPeriodString);

        // Assert: The converter should correctly identify the standard period type.
        assertEquals(expectedPeriodType, actualPeriodType);
    }
}