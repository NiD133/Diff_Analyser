package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that the range() method returns the standard ISO range for time-based fields
     * that are not explicitly handled by the Symmetry010 calendar system.
     */
    @Test
    public void range_forMicroOfSecond_returnsStandardIsoRange() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        ValueRange expectedRange = ChronoField.MICRO_OF_SECOND.range();

        // Act
        ValueRange actualRange = chronology.range(ChronoField.MICRO_OF_SECOND);

        // Assert
        assertEquals("The range for MICRO_OF_SECOND should be the standard ISO range.", expectedRange, actualRange);
    }
}