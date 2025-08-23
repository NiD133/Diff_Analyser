package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

public class InternationalFixedChronology_ESTestTest50 extends InternationalFixedChronology_ESTest_scaffolding {

    @Test
    public void range_forYearOfEra_returnsCorrectRange() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 1_000_000L);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.YEAR_OF_ERA);

        // Assert
        assertEquals("The range for YEAR_OF_ERA should match the defined constant.", expectedRange, actualRange);
    }
}