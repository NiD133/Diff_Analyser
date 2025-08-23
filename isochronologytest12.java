package org.joda.time.chrono;

import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.IllegalFieldValueException;

/**
 * Tests edge cases related to the minimum supported year in ISOChronology.
 *
 * This test class focuses on verifying the behavior of ISOChronology at its lower
 * boundary, ensuring dates are constructed correctly, calculations are accurate,
 * and boundary violations are handled properly.
 */
public class ISOChronologyMinYearTest extends TestCase {

    private static final ISOChronology UTC_CHRONOLOGY = ISOChronology.getInstanceUTC();
    private static final int MIN_YEAR = UTC_CHRONOLOGY.year().getMinimumValue();

    /**
     * Tests that the start and end dates of the minimum year are constructed correctly.
     */
    public void testMinYear_startAndEndProperties() {
        // Arrange: Define the start and end of the minimum year
        DateTime startOfYear = new DateTime(MIN_YEAR, 1, 1, 0, 0, 0, 0, UTC_CHRONOLOGY);
        DateTime endOfYear = new DateTime(MIN_YEAR, 12, 31, 23, 59, 59, 999, UTC_CHRONOLOGY);

        // Assert
        assertEquals("Year of the start date should be the minimum year", MIN_YEAR, startOfYear.getYear());
        assertEquals("Year of the end date should be the minimum year", MIN_YEAR, endOfYear.getYear());

        // The minimum year is well before the Java epoch (1970-01-01), so its millis value should be negative.
        assertTrue("Start of minimum year should be before the epoch", startOfYear.getMillis() < 0);
    }

    /**
     * Tests that the duration of the minimum year is calculated correctly, accounting for leap years.
     */
    public void testMinYear_duration() {
        // Arrange
        DateTime startOfYear = new DateTime(MIN_YEAR, 1, 1, 0, 0, 0, 0, UTC_CHRONOLOGY);
        DateTime endOfYear = new DateTime(MIN_YEAR, 12, 31, 23, 59, 59, 999, UTC_CHRONOLOGY);
        
        // Act
        long actualDurationInMillis = endOfYear.getMillis() - startOfYear.getMillis();

        // Assert: The duration should be the number of days in that year (365 or 366) in milliseconds,
        // minus one millisecond because the end date is at the last millisecond of the year.
        boolean isLeap = startOfYear.year().isLeap();
        long daysInYear = isLeap ? 366L : 365L;
        long expectedDurationInMillis = (daysInYear * DateTimeConstants.MILLIS_PER_DAY) - 1;
        
        assertEquals(expectedDurationInMillis, actualDurationInMillis);
    }

    /**
     * Tests that creating a DateTime for the minimum year from an ISO 8601 string works correctly.
     */
    public void testMinYear_parsingFromString() {
        // Arrange
        DateTime startOfYearFromComponents = new DateTime(MIN_YEAR, 1, 1, 0, 0, 0, 0, UTC_CHRONOLOGY);
        DateTime endOfYearFromComponents = new DateTime(MIN_YEAR, 12, 31, 23, 59, 59, 999, UTC_CHRONOLOGY);

        // Act
        DateTime startOfYearFromString = new DateTime(MIN_YEAR + "-01-01T00:00:00.000Z", UTC_CHRONOLOGY);
        DateTime endOfYearFromString = new DateTime(MIN_YEAR + "-12-31T23:59:59.999Z", UTC_CHRONOLOGY);

        // Assert
        assertEquals(startOfYearFromComponents, startOfYearFromString);
        assertEquals(endOfYearFromComponents, endOfYearFromString);
    }

    /**
     * Tests that attempting to go to the year before the minimum year throws an exception.
     */
    public void testMinYear_cannotDecrementBelowMinimum() {
        // Arrange
        DateTime startOfMinYear = new DateTime(MIN_YEAR, 1, 1, 0, 0, 0, 0, UTC_CHRONOLOGY);

        // Act & Assert
        try {
            startOfMinYear.minusYears(1);
            fail("Subtracting a year from the minimum year should have thrown an exception");
        } catch (IllegalFieldValueException expected) {
            // This exception is expected as we are crossing the lower boundary.
        }
    }

    /**
     * Tests an implementation detail: the behavior of the year field when given the
     * lowest possible millisecond value (Long.MIN_VALUE). This is expected to resolve
     * to the year before the minimum supported year.
     */
    public void testYearField_atLongMinValue() {
        // This tests a specific edge case of the underlying year field implementation.
        int expectedYear = MIN_YEAR - 1;
        int actualYear = UTC_CHRONOLOGY.year().get(Long.MIN_VALUE);
        assertEquals(expectedYear, actualYear);
    }
}