package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.ZoneId;
import org.junit.Test;

/**
 * This test class contains tests for the JulianChronology class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class JulianChronology_ESTestTest52 extends JulianChronology_ESTest_scaffolding {

    /**
     * Tests that calling dateNow(ZoneId) with a null argument throws a NullPointerException.
     * The method contract requires a non-null ZoneId, and this test verifies that
     * this precondition is enforced.
     */
    @Test
    public void dateNow_withNullZoneId_throwsNullPointerException() {
        // Arrange: Get an instance of the chronology to test.
        JulianChronology chronology = JulianChronology.INSTANCE;

        // Act & Assert: Call the method with a null ZoneId and verify the exception.
        try {
            chronology.dateNow((ZoneId) null);
            fail("Expected a NullPointerException to be thrown, but nothing was thrown.");
        } catch (NullPointerException e) {
            // The exception is expected. For robustness, we can also verify the message.
            // The underlying implementation uses Objects.requireNonNull(zone, "zone"),
            // which provides this specific message.
            assertEquals("zone", e.getMessage());
        }
    }
}