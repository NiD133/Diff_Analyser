package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.ZoneId;
import org.junit.Test;

/**
 * Tests for {@link Symmetry454Chronology}.
 * This class focuses on a specific test case that was improved for clarity.
 */
public class Symmetry454Chronology_ESTestTest62 extends Symmetry454Chronology_ESTest_scaffolding {

    /**
     * Tests that calling dateNow(ZoneId) with a null argument throws a NullPointerException.
     * The underlying implementation is expected to perform a null check on the zone parameter.
     */
    @Test
    public void dateNow_withNullZoneId_throwsNullPointerException() {
        // Arrange: Get the singleton instance of the chronology.
        // Using the INSTANCE field is preferred over the deprecated constructor.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // Act & Assert: Call the method with a null argument and verify the resulting exception.
        try {
            chronology.dateNow((ZoneId) null);
            fail("Expected a NullPointerException to be thrown for a null ZoneId.");
        } catch (NullPointerException expectedException) {
            // The underlying implementation uses Objects.requireNonNull(zone, "zone"),
            // so we can verify the specific message for a more robust test.
            assertEquals("zone", expectedException.getMessage());
        }
    }
}