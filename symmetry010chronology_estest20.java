package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;


/**
 * Tests for {@link Symmetry010Chronology}.
 * This focuses on handling invalid temporal inputs.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that localDateTime() throws an exception when the provided
     * temporal object does not contain time information.
     */
    @Test
    public void localDateTime_whenTemporalLacksTime_throwsException() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        // A LocalDate is a TemporalAccessor that contains date but not time information.
        TemporalAccessor temporalWithoutTime = LocalDate.of(2024, 1, 1);
        String expectedMessage = "Unable to obtain ChronoLocalDateTime from TemporalAccessor: class java.time.LocalDate";

        // Act & Assert
        // The method is expected to fail because it cannot extract time fields from a LocalDate.
        try {
            chronology.localDateTime(temporalWithoutTime);
            fail("Expected a DateTimeException to be thrown.");
        } catch (DateTimeException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    /**
     * An alternative implementation using JUnit 5's modern exception testing.
     * This is often preferred for its conciseness and clarity.
     *
     * Note: To run this, you would need the JUnit 5 (Jupiter) dependency.
     *
     * @Test
     * public void localDateTime_whenTemporalLacksTime_throwsException_junit5() {
     *     // Arrange
     *     Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
     *     TemporalAccessor temporalWithoutTime = LocalDate.of(2024, 1, 1);
     *
     *     // Act & Assert
     *     DateTimeException thrown = assertThrows(
     *         DateTimeException.class,
     *         () -> chronology.localDateTime(temporalWithoutTime)
     *     );
     *
     *     assertEquals(
     *         "Unable to obtain ChronoLocalDateTime from TemporalAccessor: class java.time.LocalDate",
     *         thrown.getMessage()
     *     );
     * }
     */
}