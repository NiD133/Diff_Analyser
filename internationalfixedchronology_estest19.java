package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.TemporalAccessor;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link InternationalFixedChronology} class, focusing on date creation.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that creating a date from a {@link TemporalAccessor} that is already an
     * {@link InternationalFixedDate} returns an equal date.
     *
     * This verifies the behavior of {@link InternationalFixedChronology#date(TemporalAccessor)}.
     */
    @Test
    public void dateFromTemporalAccessor_whenInputIsInternationalFixedDate_returnsEqualDate() {
        // Arrange
        // The International Fixed calendar has 13 months. In a non-leap year, month 12 has 29 days.
        // We use the year 2021 as a clear example of a non-leap year.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        InternationalFixedDate originalDate = chronology.date(2021, 12, 15);

        // Act
        // The method under test should correctly handle an input that is already an InternationalFixedDate.
        InternationalFixedDate convertedDate = chronology.date(originalDate);

        // Assert
        // The primary assertion is that the converted date is equal to the original.
        assertEquals("The date converted from an InternationalFixedDate should be equal to the original.",
                originalDate, convertedDate);

        // We also verify the properties of the date to confirm its integrity after conversion.
        // A non-leap year in the International Fixed calendar has 365 days.
        assertEquals("A non-leap year should have 365 days.", 365, convertedDate.lengthOfYear());
        // Month 12 in a non-leap year has 29 days.
        assertEquals("The 12th month in a non-leap year should have 29 days.", 29, convertedDate.lengthOfMonth());
    }
}