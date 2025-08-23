package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link JulianChronology#localDateTime(TemporalAccessor)}.
 */
public class JulianChronologyTest {

    /**
     * Tests that localDateTime() throws a DateTimeException when the provided
     * TemporalAccessor does not contain enough information to form a date-time.
     */
    @Test
    public void localDateTime_whenTemporalAccessorIsInsufficient_throwsException() {
        // Arrange
        JulianChronology chronology = JulianChronology.INSTANCE;
        // Use a TemporalAccessor that cannot be converted to a LocalDateTime,
        // such as an Era, to test the failure path.
        TemporalAccessor insufficientTemporal = ThaiBuddhistEra.BE;

        // Act & Assert
        DateTimeException thrown = assertThrows(
                DateTimeException.class,
                () -> chronology.localDateTime(insufficientTemporal)
        );

        // Verify the exception message to ensure it fails for the expected reason.
        assertEquals(
                "Unable to obtain ChronoLocalDateTime from TemporalAccessor: class java.time.chrono.ThaiBuddhistEra",
                thrown.getMessage()
        );
    }
}