package org.threeten.extra.chrono;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that calling date(TemporalAccessor) with an existing BritishCutoverDate
     * returns the same instance, verifying an implementation optimization.
     */
    @Test
    public void date_fromTemporalAccessor_whenInputIsBritishCutoverDate_returnsSameInstance() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        // Use a fixed, arbitrary date to make the test deterministic and easy to understand.
        BritishCutoverDate originalDate = chronology.date(2023, 10, 27);

        // Act
        // The method under test should recognize the input is already the correct type.
        BritishCutoverDate resultDate = chronology.date(originalDate);

        // Assert
        // The implementation is expected to return the exact same object, not just an equal one.
        assertSame(
                "The method should return the same instance if the input is already a BritishCutoverDate.",
                originalDate,
                resultDate);
    }
}