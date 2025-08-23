package org.joda.time;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link DateTimeComparator}.
 * This class focuses on testing the comparison logic with various field types.
 */
public class DateTimeComparatorTest {

    // Comparators for various date-time fields, initialized in setUp()
    private Comparator<Object> cMillis;
    private Comparator<Object> cSecond;
    private Comparator<Object> cMinute;
    private Comparator<Object> cHour;
    private Comparator<Object> cDayOfWeek;
    private Comparator<Object> cDayOfMonth;
    private Comparator<Object> cDayOfYear;
    private Comparator<Object> cWeekOfWeekyear;
    private Comparator<Object> cWeekyear;
    private Comparator<Object> cMonth;
    private Comparator<Object> cYear;
    private Comparator<Object> cDate;
    private Comparator<Object> cTime;

    @Before
    public void setUp() {
        // Obtain all comparator instances for different field types
        cMillis = DateTimeComparator.getInstance(null, DateTimeFieldType.secondOfMinute());
        cSecond = DateTimeComparator.getInstance(DateTimeFieldType.secondOfMinute(), DateTimeFieldType.minuteOfHour());
        cMinute = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfHour(), DateTimeFieldType.hourOfDay());
        cHour = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        cDayOfWeek = DateTimeComparator.getInstance(DateTimeFieldType.dayOfWeek(), DateTimeFieldType.weekOfWeekyear());
        cDayOfMonth = DateTimeComparator.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.monthOfYear());
        cDayOfYear = DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), DateTimeFieldType.year());
        cWeekOfWeekyear = DateTimeComparator.getInstance(DateTimeFieldType.weekOfWeekyear(), DateTimeFieldType.weekyear());
        cWeekyear = DateTimeComparator.getInstance(DateTimeFieldType.weekyear());
        cMonth = DateTimeComparator.getInstance(DateTimeFieldType.monthOfYear(), DateTimeFieldType.year());
        cYear = DateTimeComparator.getInstance(DateTimeFieldType.year());
        cDate = DateTimeComparator.getDateOnlyInstance();
        cTime = DateTimeComparator.getTimeOnlyInstance();
    }

    /**
     * Tests that all comparators return 0 when comparing two identical Long objects
     * representing the same instant.
     */
    @Test
    public void testCompare_withEqualLongObjects_returnsZero() {
        // Arrange: Use a fixed, deterministic timestamp for test repeatability.
        long fixedTimestamp = new DateTime(2005, 6, 9, 10, 20, 30, 40, DateTimeZone.UTC).getMillis();
        Long timestamp1 = Long.valueOf(fixedTimestamp);
        Long timestamp2 = Long.valueOf(fixedTimestamp);

        // Group comparators to test them systematically.
        Map<String, Comparator<Object>> comparators = new LinkedHashMap<>();
        comparators.put("Millis", cMillis);
        comparators.put("Second", cSecond);
        comparators.put("Minute", cMinute);
        comparators.put("Hour", cHour);
        comparators.put("DayOfWeek", cDayOfWeek);
        comparators.put("DayOfMonth", cDayOfMonth);
        comparators.put("DayOfYear", cDayOfYear);
        comparators.put("WeekOfWeekyear", cWeekOfWeekyear);
        comparators.put("Weekyear", cWeekyear);
        comparators.put("Month", cMonth);
        comparators.put("Year", cYear);
        comparators.put("Date-only", cDate);
        comparators.put("Time-only", cTime);

        // Act & Assert: Verify that all comparators report the two equal Longs as equal.
        for (Map.Entry<String, Comparator<Object>> entry : comparators.entrySet()) {
            String comparatorName = entry.getKey();
            Comparator<Object> comparator = entry.getValue();
            
            int result = comparator.compare(timestamp1, timestamp2);
            
            assertEquals(comparatorName + " comparator should return 0 for equal Longs", 0, result);
        }
    }
}