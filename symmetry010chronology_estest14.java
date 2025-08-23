package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link Symmetry010Chronology} class.
 * This focuses on creating dates from other calendar systems.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that a {@link Symmetry010Date} can be correctly created from a date
     * in a different calendar system, specifically {@link CopticDate}.
     *
     * The core contract of {@link Symmetry010Chronology#date(TemporalAccessor)} is that
     * the resulting date must represent the same point in the timeline (i.e., have the
     * same epoch day) as the source date.
     */
    @Test
    public void shouldCreateEquivalentDateWhenConvertingFromCopticDate() {
        // Arrange
        // The epoch day -4481L corresponds to the ISO date 1957-09-11.
        // We use a CopticDate as the input TemporalAccessor from a different chronology.
        long epochDay = -4481L;
        CopticDate inputCopticDate = CopticDate.ofEpochDay(epochDay);
        Symmetry010Chronology symmetryChronology = Symmetry010Chronology.INSTANCE;

        // Act
        // Convert the CopticDate to a Symmetry010Date using the chronology.
        Symmetry010Date actualSymmetryDate = symmetryChronology.date(inputCopticDate);

        // Assert
        // 1. The primary assertion: the resulting date must have the same epoch day.
        // This confirms the conversion correctly preserves the point in time.
        assertEquals("The epoch day should be preserved after conversion",
                epochDay, actualSymmetryDate.toEpochDay());

        // 2. A secondary assertion (from the original test): the era should be correct.
        // A date corresponding to 1957 is in the Common Era (CE).
        assertEquals("The era should be Common Era (CE)",
                IsoEra.CE, actualSymmetryDate.getEra());
    }
}