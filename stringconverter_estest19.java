package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.MutableInterval;
import org.joda.time.chrono.IslamicChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link StringConverter}.
 * This focuses on the setInto(ReadWritableInterval, Object, Chronology) method.
 */
public class StringConverter_ESTestTest19 { // Note: Class name kept from original for context.

    /**
     * Verifies that setInto() correctly parses an interval string using a specific,
     * non-default chronology. The string "6/8" should be interpreted as an interval
     * from the start of year 6 to the start of year 8 in the provided Islamic calendar.
     */
    @Test
    public void setIntoShouldParseIntervalStringUsingProvidedChronology() {
        // Arrange
        final String intervalString = "6/8";
        final Chronology islamicChronology = IslamicChronology.getInstanceUTC();
        final StringConverter converter = StringConverter.INSTANCE;

        // An empty interval that will be modified by the converter.
        final MutableInterval intervalToUpdate = new MutableInterval(0L, 0L);

        // For clarity, explicitly define the expected start and end times instead of using magic numbers.
        // In the Islamic calendar, "6" is parsed as the beginning of year 6.
        final DateTime expectedStart = new DateTime(6, 1, 1, 0, 0, islamicChronology);
        final DateTime expectedEnd = new DateTime(8, 1, 1, 0, 0, islamicChronology);

        // Act
        converter.setInto(intervalToUpdate, intervalString, islamicChronology);

        // Assert
        assertEquals("The start of the interval should be updated to the start of year 6 in the Islamic calendar.",
                expectedStart.getMillis(), intervalToUpdate.getStartMillis());
        assertEquals("The end of the interval should be updated to the start of year 8 in the Islamic calendar.",
                expectedEnd.getMillis(), intervalToUpdate.getEndMillis());
    }
}