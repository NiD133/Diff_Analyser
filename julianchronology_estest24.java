package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JulianChronology_ESTestTest24 extends JulianChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void dateEpochDay_whenEpochDayIsTooLarge_throwsDateTimeException() {
        // Arrange
        JulianChronology chronology = JulianChronology.INSTANCE;
        // This epoch day corresponds to the year 1,001,969, which is outside the
        // supported range of -999,998 to 999,999 for the Julian calendar.
        long epochDayBeyondMaxRange = 365_250_000L;
        String expectedErrorMessage = "Invalid value for Year (valid values -999998 - 999999): 1001969";

        // Act & Assert
        try {
            chronology.dateEpochDay(epochDayBeyondMaxRange);
            fail("Expected a DateTimeException because the epoch day is outside the supported year range.");
        } catch (DateTimeException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}