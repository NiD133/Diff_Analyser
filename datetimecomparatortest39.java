package org.joda.time;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    /**
     * Tests that the date-only comparator correctly sorts a list of DateTime objects
     * based on their date, while ignoring their time-of-day components.
     */
    @Test
    public void testDateOnlyComparator_sortsListByDateIgnoringTime() {
        // Arrange: Create DateTime objects with different dates and varying times to ensure
        // the time part is properly ignored during comparison.
        DateTime dt_1066 = new DateTime("1066-09-22T12:00:00", UTC);
        DateTime dt_1776 = new DateTime("1776-12-25T04:05:06", UTC);
        DateTime dt_1863 = new DateTime("1863-01-31T06:07:08", UTC);
        DateTime dt_1998 = new DateTime("1998-10-03T01:02:03", UTC);
        DateTime dt_1999 = new DateTime("1999-02-01T10:20:30", UTC);
        DateTime dt_2100 = new DateTime("2100-07-04T18:19:20", UTC);
        DateTime dt_2525 = new DateTime("2525-05-20T23:59:59", UTC);

        List<DateTime> dateTimes = new ArrayList<>(Arrays.asList(
            dt_1999, dt_1998, dt_2525, dt_1776, dt_1863, dt_1066, dt_2100
        ));

        // The comparator under test will only consider the date part.
        Comparator<Object> dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();

        // Act: Sort the list using the date-only comparator.
        Collections.sort(dateTimes, dateOnlyComparator);

        // Assert: The list should now be in chronological order by date.
        // The expected order is defined explicitly for a clear and direct comparison.
        List<DateTime> expectedSortedDateTimes = Arrays.asList(
            dt_1066, dt_1776, dt_1863, dt_1998, dt_1999, dt_2100, dt_2525
        );

        assertEquals(expectedSortedDateTimes, dateTimes);
    }
}