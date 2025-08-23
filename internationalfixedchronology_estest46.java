package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class contains tests for the {@link InternationalFixedChronology} class.
 * This specific test focuses on the localDateTime(TemporalAccessor) method.
 */
public class InternationalFixedChronology_ESTestTest46 extends InternationalFixedChronology_ESTest_scaffolding {

    /**
     * Tests the conversion of a standard {@code LocalDateTime} to an
     * {@code InternationalFixedChronology} {@code ChronoLocalDateTime}.
     * <p>
     * The test verifies that the date and time components are correctly
     * translated according to the rules of the International Fixed Calendar.
     */
    @Test
    public void localDateTime_fromIsoLocalDateTime_convertsSuccessfully() {
        // Arrange: Use the singleton instance and a fixed ISO LocalDateTime.
        // The chosen date, 2023-02-01, is the 32nd day of a non-leap year.
        // In the International Fixed Calendar (13 months of 28 days), this corresponds
        // to the 4th day of the 2nd month (since 32 = 28 + 4).
        InternationalFixedChronology ifc = InternationalFixedChronology.INSTANCE;
        LocalDateTime isoLocalDateTime = LocalDateTime.of(2023, 2, 1, 15, 30);

        // Act: Convert the ISO LocalDateTime to the International Fixed Chronology.
        ChronoLocalDateTime<InternationalFixedDate> ifcLocalDateTime = ifc.localDateTime(isoLocalDateTime);

        // Assert: Verify the converted date and time are correct.
        assertNotNull("The converted date-time should not be null.", ifcLocalDateTime);
        assertEquals("The chronology of the result should be InternationalFixedChronology.",
                ifc, ifcLocalDateTime.getChronology());

        // Verify date components based on the expected conversion
        assertEquals("Year should be preserved.", 2023, ifcLocalDateTime.get(ChronoField.YEAR));
        assertEquals("Month should be calculated correctly.", 2, ifcLocalDateTime.get(ChronoField.MONTH_OF_YEAR));
        assertEquals("Day of month should be calculated correctly.", 4, ifcLocalDateTime.get(ChronoField.DAY_OF_MONTH));

        // Verify time components are preserved
        assertEquals("Hour should be preserved.", 15, ifcLocalDateTime.get(ChronoField.HOUR_OF_DAY));
        assertEquals("Minute should be preserved.", 30, ifcLocalDateTime.get(ChronoField.MINUTE_OF_HOUR));
    }
}