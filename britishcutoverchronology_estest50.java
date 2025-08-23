package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    @Test
    public void eraOf_forValue0_returnsBCEra() {
        // Arrange
        // The integer value 0 represents the 'Before Christ' (BC) era.
        final int eraValue = 0;
        final BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Act
        // The eraOf(int) method converts an integer to an Era object.
        final JulianEra actualEra = chronology.eraOf(eraValue);

        // Assert
        assertEquals("The era for value 0 should be BC", JulianEra.BC, actualEra);
    }
}