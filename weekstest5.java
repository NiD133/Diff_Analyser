package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link Weeks} class, focusing on the
 * {@link Weeks#weeksIn(ReadableInterval)} factory method.
 */
public class WeeksTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    /**
     * Tests that a null interval results in zero weeks.
     */
    @Test
    public void weeksIn_givenNullInterval_returnsZero() {
        assertEquals("A null interval should result in zero weeks.", Weeks.ZERO, Weeks.weeksIn(null));
    }

    /**
     * Tests that a zero-duration interval (where start and end are the same)
     * results in zero weeks.
     */
    @Test
    public void weeksIn_givenZeroDurationInterval_returnsZero() {
        // Arrange
        DateTime pointInTime = new DateTime(2023, 10, 27, 12, 0, PARIS);
        Interval zeroDurationInterval = new Interval(pointInTime, pointInTime);

        // Act
        Weeks result = Weeks.weeksIn(zeroDurationInterval);

        // Assert
        assertEquals(Weeks.ZERO, result);
    }

    /**
     * Tests that the method correctly calculates the number of *completed* weeks
     * for various interval durations.
     */
    @Test
    public void weeksIn_givenNonZeroDurationInterval_returnsCompletedWeeks() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 0, PARIS);

        // Case 1: An interval of exactly 3 weeks.
        DateTime endAfterThreeWeeks = start.plusWeeks(3);
        Interval threeWeekInterval = new Interval(start, endAfterThreeWeeks);

        // Case 2: An interval of exactly 6 weeks.
        DateTime endAfterSixWeeks = start.plusWeeks(6);
        Interval sixWeekInterval = new Interval(start, endAfterSixWeeks);

        // Case 3: An interval just shy of 4 weeks, which should truncate to 3.
        DateTime endAfterAlmostFourWeeks = start.plusWeeks(4).minusDays(1);
        Interval almostFourWeeksInterval = new Interval(start, endAfterAlmostFourWeeks);

        // Act & Assert
        assertEquals("An interval of exactly 3 weeks should result in 3 weeks.",
                Weeks.weeks(3), Weeks.weeksIn(threeWeekInterval));

        assertEquals("An interval of exactly 6 weeks should result in 6 weeks.",
                Weeks.weeks(6), Weeks.weeksIn(sixWeekInterval));

        assertEquals("An interval of 3 weeks and 6 days should truncate to 3 weeks.",
                Weeks.weeks(3), Weeks.weeksIn(almostFourWeeksInterval));
    }
}