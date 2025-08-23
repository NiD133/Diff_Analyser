package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link EthiopicChronology}.
 * This test focuses on boundary conditions for method inputs.
 */
public class EthiopicChronologyTest {

    /**
     * Verifies that isLeapDay() throws an IllegalArgumentException when the provided
     * instant is below the minimum supported value for the Ethiopic calendar.
     */
    @Test
    public void isLeapDay_whenInstantIsBelowMinimum_throwsIllegalArgumentException() {
        // Arrange: Create a chronology and define an instant that is known to be
        // out of the valid range (specifically, Long.MIN_VALUE).
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        final long instantBelowSupportedMinimum = Long.MIN_VALUE;

        // Act & Assert: Attempt to use the invalid instant and verify that the
        // expected exception is thrown with a descriptive message.
        try {
            chronology.isLeapDay(instantBelowSupportedMinimum);
            fail("Expected an IllegalArgumentException because the instant is below the supported minimum.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception is thrown for the correct reason by checking its message.
            String expectedMessageContent = "The instant is below the supported minimum";
            assertTrue(
                "The exception message should explain that the instant is too low.",
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}