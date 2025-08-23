package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the conversion of temporal objects to Julian date-times
 * within the {@link JulianChronology}.
 */
public class JulianChronologyLocalDateTimeTest {

    /**
     * Tests that a standard ISO LocalDateTime is correctly converted to a
     * Julian ChronoLocalDateTime.
     */
    @Test
    public void localDateTime_shouldConvertIsoLocalDateTimeToJulian() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        // A fixed, known LocalDateTime in the standard ISO calendar system.
        LocalDateTime isoLocalDateTime = LocalDateTime.of(2014, 2, 15, 10, 30, 45);

        // Act
        ChronoLocalDateTime<JulianDate> result = julianChronology.localDateTime(isoLocalDateTime);

        // Assert
        assertNotNull("The conversion result should not be null.", result);

        // The ISO date 2014-02-15 corresponds to the Julian date 2014-02-02.
        // The time component should remain unchanged.
        JulianDate expectedJulianDate = JulianDate.of(2014, 2, 2);
        LocalTime expectedTime = LocalTime.of(10, 30, 45);
        ChronoLocalDateTime<JulianDate> expected = expectedJulianDate.atTime(expectedTime);

        assertEquals("The converted Julian date-time should match the expected value.", expected, result);
    }
}