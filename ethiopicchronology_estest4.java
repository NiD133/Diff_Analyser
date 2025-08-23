package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link EthiopicChronology}.
 */
public class EthiopicChronologyTest {

    /**
     * Verifies that getApproxMillisAtEpochDividedByTwo() returns the correct pre-calculated constant.
     * <p>
     * This test improves understandability by re-deriving the "magic number" constant from its
     * logical components. This makes the test self-documenting and verifies the logic behind
     * the value, rather than just checking for a hardcoded number.
     */
    @Test
    public void getApproxMillisAtEpochDividedByTwo_shouldReturnCorrectApproximation() {
        // Arrange
        // Use the UTC instance for consistency, as the calculation is time-zone independent.
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();

        // Act
        long actualValue = chronology.getApproxMillisAtEpochDividedByTwo();

        // Assert
        // The expected value is derived from the number of days between the Ethiopic epoch
        // (start of year 1, which is 8 CE) and the Unix epoch (1970 CE).
        // The Ethiopic calendar is Julian-like, with a leap year every 4 years.
        final long yearsBetweenEpochs = 1970L - 8L; // Ethiopic year 1 is 8 CE
        final long leapYears = yearsBetweenEpochs / 4L;
        final long totalDays = (yearsBetweenEpochs * 365L) + leapYears;
        final long totalMillisAtEpoch = totalDays * DateTimeConstants.MILLIS_PER_DAY;
        final long expectedValue = totalMillisAtEpoch / 2L;

        assertEquals(expectedValue, actualValue);
    }
}