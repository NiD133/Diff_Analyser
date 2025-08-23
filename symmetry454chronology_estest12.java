package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.IsoEra;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    @Test
    public void dateNow_withZoneId_returnsDateInCorrectEra() {
        // Arrange: Get the singleton instance of the chronology and a specific ZoneId.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        ZoneId zone = ZoneOffset.MIN;

        // Act: Obtain the current date in the Symmetry454 calendar system for the given zone.
        Symmetry454Date currentDate = chronology.dateNow(zone);

        // Assert: Verify that the current date falls within the Common Era (CE).
        // This is a basic sanity check for the dateNow() method.
        assertEquals("The era of the current date should be CE", IsoEra.CE, currentDate.getEra());
    }
}