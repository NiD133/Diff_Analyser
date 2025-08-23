package org.joda.time.convert;

import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the period conversion functionality in {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that setInto() correctly parses an ISO8601 period string and
     * completely overwrites the values of an existing MutablePeriod.
     * This includes resetting fields not specified in the string to zero.
     */
    @Test
    public void setInto_forPeriod_shouldParseStringAndOverwriteExistingValues() {
        // Arrange
        final String periodString = "P2Y4W3D"; // Represents 2 years, 4 weeks, and 3 days

        // A period with pre-existing values to ensure they are overwritten, not just added to.
        // The PeriodType only supports years, weeks, days, and time components.
        final PeriodType periodType = PeriodType.yearWeekDayTime();
        MutablePeriod periodToModify = new MutablePeriod(1, 0, 1, 1, 1, 1, 1, 1, periodType);

        // The expected state of the period after the conversion.
        // Note that the time fields (hours, minutes, seconds, millis) are reset to zero.
        MutablePeriod expectedPeriod = new MutablePeriod(2, 0, 4, 3, 0, 0, 0, 0, periodType);

        // Act
        StringConverter.INSTANCE.setInto(periodToModify, periodString, null);

        // Assert
        assertEquals(expectedPeriod, periodToModify);
    }
}