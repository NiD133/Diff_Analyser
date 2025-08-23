package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Symmetry454Chronology#localDateTime(java.time.temporal.TemporalAccessor)}.
 */
public class Symmetry454Chronology_ESTestTest5 { // Retaining original class name for context

    @Test
    public void localDateTime_fromIsoLocalDateTime_convertsSuccessfully() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        // Use a fixed, non-mocked date-time for a deterministic and clear test case.
        LocalDateTime isoLocalDateTime = LocalDateTime.of(2024, 7, 22, 12, 30, 45);

        // Act
        // Convert the standard ISO LocalDateTime to the Symmetry454 calendar system.
        ChronoLocalDateTime<Symmetry454Date> symmetryLocalDateTime = chronology.localDateTime(isoLocalDateTime);

        // Assert
        assertNotNull("The converted ChronoLocalDateTime should not be null.", symmetryLocalDateTime);

        // Verify that the date part has been converted to the target chronology.
        assertTrue("The date part should be an instance of Symmetry454Date.",
                symmetryLocalDateTime.toLocalDate() instanceof Symmetry454Date);

        // According to the ChronoLocalDateTime contract, the time part should be preserved.
        assertEquals("The time part should remain unchanged after conversion.",
                isoLocalDateTime.toLocalTime(), symmetryLocalDateTime.toLocalTime());
    }
}