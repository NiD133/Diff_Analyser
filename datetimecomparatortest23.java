package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import org.junit.Test;

/**
 * Unit tests for {@link DateTimeComparator} focused on comparing by week of weekyear.
 */
public class DateTimeComparatorWeekOfWeekyearTest {

    // A comparator that only considers the 'week of weekyear' field.
    // The lower limit is inclusive, and the upper limit is exclusive.
    // This setup isolates the comparison to just the weekOfWeekyear field.
    private static final Comparator<Object> WEEK_OF_WEEKYEAR_COMPARATOR =
        DateTimeComparator.getInstance(DateTimeFieldType.weekOfWeekyear(), DateTimeFieldType.weekyear());

    @Test
    public void compareShouldReturnZeroForDatesInSameWeek() {
        // Both dates are in week 1 of 2000.
        DateTime date1 = new DateTime("2000-01-04T10:00:00Z"); // Tuesday
        DateTime date2 = new DateTime("2000-01-05T20:00:00Z"); // Wednesday

        assertEquals("Dates within the same week should be considered equal",
            0, WEEK_OF_WEEKYEAR_COMPARATOR.compare(date1, date2));
    }

    @Test
    public void compareShouldReturnNegativeWhenFirstDateIsInEarlierWeek() {
        // dateInWeek1 is in week 1 of 2000.
        DateTime dateInWeek1 = new DateTime("2000-01-04T00:00:00Z");
        // dateInWeek2 is in week 2 of 2000.
        DateTime dateInWeek2 = new DateTime("2000-01-11T00:00:00Z");

        assertTrue("Comparing an earlier week to a later week should return a negative number",
            WEEK_OF_WEEKYEAR_COMPARATOR.compare(dateInWeek1, dateInWeek2) < 0);
    }

    @Test
    public void compareShouldReturnPositiveWhenFirstDateIsInLaterWeek() {
        // dateInWeek2 is in week 2 of 2000.
        DateTime dateInWeek2 = new DateTime("2000-01-11T00:00:00Z");
        // dateInWeek1 is in week 1 of 2000.
        DateTime dateInWeek1 = new DateTime("2000-01-04T00:00:00Z");

        assertTrue("Comparing a later week to an earlier week should return a positive number",
            WEEK_OF_WEEKYEAR_COMPARATOR.compare(dateInWeek2, dateInWeek1) > 0);
    }

    @Test
    public void compareShouldOnlyConsiderWeekNumberAcrossYearBoundaries() {
        // dateInWeek1 is in week 1 of weekyear 2000.
        DateTime dateInWeek1 = new DateTime("2000-01-04T00:00:00Z");
        // dateInWeek52 is in week 52 of weekyear 1999.
        DateTime dateInWeek52 = new DateTime("1999-12-31T00:00:00Z");

        // The comparator only checks the week number (1 vs 52), ignoring the year.
        assertTrue("Comparing week 1 to week 52 should return a negative number",
            WEEK_OF_WEEKYEAR_COMPARATOR.compare(dateInWeek1, dateInWeek52) < 0);

        assertTrue("Comparing week 52 to week 1 should return a positive number",
            WEEK_OF_WEEKYEAR_COMPARATOR.compare(dateInWeek52, dateInWeek1) > 0);
    }

    @Test
    public void testToString() {
        assertEquals(
            "DateTimeComparator[weekOfWeekyear-weekyear]",
            WEEK_OF_WEEKYEAR_COMPARATOR.toString()
        );
    }
}