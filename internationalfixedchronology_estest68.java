package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link InternationalFixedChronology#date(java.time.temporal.TemporalAccessor)}.
 */
public class InternationalFixedChronology_ESTestTest68 {

    /**
     * Tests that creating a date from a TemporalAccessor that is already an
     * InternationalFixedDate returns an equal InternationalFixedDate.
     */
    @Test
    public void dateFromTemporalAccessor_givenInternationalFixedDate_returnsEqualDate() {
        // Arrange
        InternationalFixedChronology ifcChronology = InternationalFixedChronology.INSTANCE;
        
        // The original test used a complex calculation resulting in a date with an epoch day
        // of -58199. We create this date directly for a clear and stable test setup.
        InternationalFixedDate originalDate = InternationalFixedDate.ofEpochDay(-58199L);

        // Act
        // Call the method under test, which should correctly handle an input that is
        // already of the target chronology and type.
        InternationalFixedDate convertedDate = ifcChronology.date(originalDate);

        // Assert
        // The chronology should recognize the input and return an equal date instance.
        assertEquals("The converted date should be equal to the original date.", originalDate, convertedDate);
    }
}