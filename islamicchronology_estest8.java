package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link IslamicChronology} class, focusing on understandability.
 */
public class IslamicChronologyTest {

    /**
     * Tests that getYearDifference correctly calculates the difference in years
     * between two instants. The method's behavior is defined as
     * getYear(minuendInstant) - getYear(subtrahendInstant).
     *
     * This test verifies a case where the minuend instant is earlier than the subtrahend,
     * resulting in a negative year difference.
     */
    @Test
    public void getYearDifference_withMinuendBeforeSubtrahend_returnsNegativeDifference() {
        // Arrange
        // Get an instance of the IslamicChronology in the UTC time zone.
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();

        // Create a minuend date in the Islamic year 1389 AH.
        DateTime minuendDate = new DateTime(1389, 1, 1, 0, 0, islamicChronology);

        // Create a subtrahend date in the Islamic year 1391 AH.
        DateTime subtrahendDate = new DateTime(1391, 1, 1, 0, 0, islamicChronology);

        // Act
        // Calculate the difference in years between the two instants.
        long yearDifference = islamicChronology.getYearDifference(
                minuendDate.getMillis(),
                subtrahendDate.getMillis()
        );

        // Assert
        // The expected difference is the minuend year minus the subtrahend year (1389 - 1391).
        assertEquals("The year difference should be -2", -2L, yearDifference);
    }
}