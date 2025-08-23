package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * This test verifies the behavior of getDaysInMonthMax() when provided with an
     * invalid, negative month number.
     *
     * <p>The current implementation does not validate the month input. For an even
     * number (like -238), the internal calculation results in the length of a short
     * Islamic month, which is 29 days. This test documents that specific behavior.
     */
    @Test
    public void getDaysInMonthMax_shouldReturn29_forInvalidEvenMonth() {
        // Arrange
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        int invalidEvenMonth = -238;
        final int ISLAMIC_SHORT_MONTH_LENGTH = 29;

        // Act
        int actualMaxDays = chronology.getDaysInMonthMax(invalidEvenMonth);

        // Assert
        assertEquals(ISLAMIC_SHORT_MONTH_LENGTH, actualMaxDays);
    }
}