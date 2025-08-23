package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoPeriod;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link BritishCutoverChronology}.
 * This class demonstrates an improved version of a formerly unclear, auto-generated test.
 */
public class BritishCutoverChronology_ESTestTest39 {

    /**
     * Tests that subtracting a ChronoPeriod from a BritishCutoverDate produces the correct result.
     */
    @Test
    public void minus_whenSubtractingChronoPeriod_thenReturnsCorrectDate() {
        // Arrange
        // The original test used obscure numbers (e.g., epoch day 371) and a very large,
        // arbitrary period (371 years, 371 months, 371 days). This made the test's
        // purpose and correctness difficult to verify.
        // This version uses clear, human-readable dates and a simple period.
        BritishCutoverDate initialDate = BritishCutoverDate.of(1971, 1, 6);
        ChronoPeriod periodToSubtract = BritishCutoverChronology.INSTANCE.period(1, 2, 3); // P1Y2M3D

        // Calculate the expected result for clarity and correctness.
        // 1971-01-06 minus 1 year   -> 1970-01-06
        // 1970-01-06 minus 2 months -> 1969-11-06
        // 1969-11-06 minus 3 days   -> 1969-11-03
        BritishCutoverDate expectedDate = BritishCutoverDate.of(1969, 11, 3);

        // Act
        BritishCutoverDate actualDate = initialDate.minus(periodToSubtract);

        // Assert
        // The original test only checked that the result was different from the input,
        // which is a weak assertion. This improved test verifies that the subtraction
        // yields the correct date, making the test more robust and valuable.
        assertEquals(expectedDate, actualDate);
    }
}