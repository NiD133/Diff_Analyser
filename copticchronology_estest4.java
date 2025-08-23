package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link CopticChronology} class, focusing on boundary conditions.
 */
public class CopticChronologyTest {

    @Test
    public void isLeapDay_throwsIllegalArgumentException_whenInstantIsBelowMinimum() {
        // Arrange: Get a CopticChronology instance and define an instant that is
        // known to be below the minimum supported value. Using UTC makes the test
        // deterministic and independent of the system's default time zone.
        CopticChronology copticChronology = CopticChronology.getInstance(DateTimeZone.UTC);
        long instantBelowMinimum = Long.MIN_VALUE;

        // Act & Assert: Attempt to call the method with the invalid instant and
        // verify that the correct exception is thrown.
        try {
            copticChronology.isLeapDay(instantBelowMinimum);
            fail("Expected an IllegalArgumentException because the instant is below the supported minimum.");
        } catch (IllegalArgumentException e) {
            // This is the expected outcome.
            // For a more robust test, verify the exception message to ensure the
            // failure is for the correct reason.
            String expectedMessage = "The instant is below the supported minimum of 0001-01-01T00:00:00.000Z (CopticChronology[UTC])";
            assertEquals("The exception message should clearly state the reason for the failure.",
                         expectedMessage, e.getMessage());
        }
    }
}