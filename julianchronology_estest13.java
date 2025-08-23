package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link JulianChronology}.
 * This test focuses on exception handling when creating a ZonedDateTime from a partial TemporalAccessor.
 */
public class JulianChronologyTest {

    /**
     * Tests that creating a ZonedDateTime from a TemporalAccessor that lacks
     * date and time information throws a DateTimeException.
     */
    @Test
    public void zonedDateTime_whenTemporalAccessorLacksDateTimeInfo_throwsException() {
        // Arrange: A TemporalAccessor containing only zone information is insufficient
        // to create a full ZonedDateTime.
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        TemporalAccessor insufficientTemporal = ZoneOffset.MAX;

        // Act & Assert: Attempting the conversion should result in a DateTimeException.
        try {
            julianChronology.zonedDateTime(insufficientTemporal);
            fail("Expected a DateTimeException to be thrown, but no exception was thrown.");
        } catch (DateTimeException e) {
            // Verify that the exception message clearly explains the problem.
            assertEquals(
                "Unable to obtain ChronoZonedDateTime from TemporalAccessor: class java.time.ZoneOffset",
                e.getMessage()
            );
        }
    }
}