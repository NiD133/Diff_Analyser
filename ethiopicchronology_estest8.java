package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// The original test class name and inheritance are kept for context.
// Unused imports from the original test have been removed.
public class EthiopicChronology_ESTestTest8 extends EthiopicChronology_ESTest_scaffolding {

    /**
     * Tests that the first day of the first year in the Ethiopic calendar
     * corresponds to its known epoch date in the Julian calendar.
     */
    @Test
    public void calculateFirstDayOfYearMillis_forYear1_shouldCorrespondToJulianEpoch() {
        // Arrange
        // According to the EthiopicChronology documentation, Year 1 began on August 29, 8 CE (Julian).
        // We create this date in the Julian calendar to derive the expected millisecond value,
        // making the test self-documenting and removing the magic number.
        DateTime expectedStartOfEra = new DateTime(8, 8, 29, 0, 0, 0, 0, JulianChronology.getInstanceUTC());
        long expectedFirstDayMillis = expectedStartOfEra.getMillis();

        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();

        // Act
        // Calculate the start of the first year (year 1) in the Ethiopic calendar.
        long actualFirstDayMillis = ethiopicChronology.calculateFirstDayOfYearMillis(1);

        // Assert
        assertEquals(
            "The start of Ethiopic year 1 should match the epoch date of August 29, 8 CE (Julian).",
            expectedFirstDayMillis,
            actualFirstDayMillis
        );
    }
}