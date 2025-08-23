package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void range_forMilliOfSecond_returnsCorrectRange() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(0, 999);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.MILLI_OF_SECOND);

        // Assert
        assertEquals("The range for MILLI_OF_SECOND should be consistent with the ISO standard.",
                expectedRange, actualRange);
    }
}