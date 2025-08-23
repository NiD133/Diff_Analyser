package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that getYear() correctly calculates a proleptic (pre-epoch) year for a given
     * millisecond instant.
     *
     * <p>The Islamic calendar's official starting point is year 1 AH. However, the underlying
     * arithmetic for {@code getYear(long)} can be extrapolated backwards into proleptic years.
     * This test verifies that a timestamp from before the standard epoch (1970-01-01) and
     * before year 1 AH correctly resolves to the expected negative Islamic year.</p>
     */
    @Test
    public void getYear_forTimestampBeforeEpoch_returnsCorrectProlepticNegativeYear() {
        // Arrange
        // Use the UTC instance for deterministic results, avoiding reliance on the system's default time zone.
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();

        // This specific timestamp corresponds to the Gregorian date -0198-09-19T11:59:56.766Z.
        // Based on the Islamic calendar's arithmetic rules, this instant is expected to fall
        // within the proleptic Islamic year -839 AH.
        long instantInMillis = -69139612803234L;
        int expectedYear = -839;

        // Act
        int actualYear = islamicChronology.getYear(instantInMillis);

        // Assert
        assertEquals("The calculated Islamic year should match the expected proleptic year",
                expectedYear, actualYear);
    }
}