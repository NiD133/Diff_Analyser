package org.joda.time;

import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import org.junit.Test;

/**
 * Unit tests for {@link DateTimeComparator}, focusing on its handling of null inputs.
 */
public class DateTimeComparatorTest {

    // A fixed point in time in the past, used for comparison against "now".
    // The 'Z' indicates UTC, making the test independent of the system's default time zone.
    private static final ReadableInstant PAST_INSTANT = new DateTime("2000-01-01T00:00:00Z");

    /**
     * Tests that the comparator correctly handles null inputs.
     * According to the Joda-Time {@link DateTimeComparator} contract, a null object
     * is treated as the current system time (i.e., "now").
     */
    @Test
    public void testCompareWithNulls() {
        // This test assumes it runs after the year 2000.
        // Therefore, "now" (represented by null) should be greater than PAST_INSTANT.
        Comparator<Object> yearComparator = DateTimeComparator.getInstance(DateTimeFieldType.year());

        // Assertion 1: A null (now) on the left-hand side should be greater than a past date.
        // compare(now, past) > 0
        assertTrue("A null should be treated as 'now' and be greater than a past date.",
                   yearComparator.compare(null, PAST_INSTANT) > 0);

        // Assertion 2: A past date should be less than a null (now) on the right-hand side.
        // compare(past, now) < 0
        assertTrue("A past date should be less than null (treated as 'now').",
                   yearComparator.compare(PAST_INSTANT, null) < 0);
    }
}