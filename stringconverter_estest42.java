package org.joda.time.convert;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    @Test
    public void getInstantMillis_whenParsingYearOnlyString_shouldReturnStartOfYearInUTC() {
        // Arrange
        final String yearOnlyString = "000";
        final StringConverter converter = StringConverter.INSTANCE;
        final ISOChronology isoChronologyUTC = ISOChronology.getInstanceUTC();

        // The default Joda-Time ISO parser interprets a short numeric string like "000"
        // as a year, defaulting to the first day of that year at midnight.
        // We create an expected DateTime object to make the assertion clear and avoid magic numbers.
        final DateTime expectedDateTime = new DateTime(0, 1, 1, 0, 0, isoChronologyUTC);
        final long expectedMillis = expectedDateTime.getMillis();

        // Act
        final long actualMillis = converter.getInstantMillis(yearOnlyString, isoChronologyUTC);

        // Assert
        assertEquals(expectedMillis, actualMillis);
    }
}