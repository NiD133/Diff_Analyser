package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assume.assumeFalse;

/**
 * Test suite focusing on the behavior of the {@link GregorianChronology#withUTC()} method.
 */
public class GregorianChronologyWithUTCMethodTest {

    // A specific non-UTC time zone to ensure the test is deterministic and not
    // dependent on the default system time zone.
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    /**
     * Verifies that calling withUTC() on a chronology with a non-UTC time zone
     * returns a new Chronology instance.
     */
    @Test
    public void withUTC_shouldReturnNewInstance_whenChronologyIsNotInUTC() {
        // Arrange
        // Precondition: Ensure the test environment is not configured in a way that makes Paris UTC.
        assumeFalse("The selected test time zone must not be UTC.", PARIS.equals(DateTimeZone.UTC));
        
        // Create a GregorianChronology instance with a specific non-UTC time zone.
        // The original test used 7 for minDaysInFirstWeek, so we preserve that detail.
        final int minDaysInFirstWeek = 7;
        GregorianChronology chronologyInParisZone = GregorianChronology.getInstance(PARIS, minDaysInFirstWeek);

        // Act
        // Request the UTC equivalent of the chronology.
        Chronology utcChronology = chronologyInParisZone.withUTC();

        // Assert
        // The method should return a new object because the time zone was changed.
        assertNotSame(
            "withUTC() must return a new instance when the original zone is not UTC.",
            chronologyInParisZone,
            utcChronology
        );
    }

    /**
     * Verifies that calling withUTC() on a chronology that is already in UTC
     * returns the same instance as an optimization.
     */
    @Test
    public void withUTC_shouldReturnSameInstance_whenChronologyIsAlreadyInUTC() {
        // Arrange
        // Create a GregorianChronology instance that is already in the UTC time zone.
        final int minDaysInFirstWeek = 7;
        GregorianChronology chronologyInUTC = GregorianChronology.getInstance(DateTimeZone.UTC, minDaysInFirstWeek);

        // Act
        // Request the UTC equivalent of the chronology.
        Chronology resultChronology = chronologyInUTC.withUTC();

        // Assert
        // The method should return the same object as no change was needed.
        assertSame(
            "withUTC() should return the same instance when the original zone is already UTC.",
            chronologyInUTC,
            resultChronology
        );
    }
}