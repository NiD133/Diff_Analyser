package org.joda.time.convert;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.MutableInterval;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the setInto(ReadWritableInterval, ...) method of {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that StringConverter can correctly parse an ISO 8601 interval string
     * defined by a period and an end instant.
     */
    @Test
    public void setIntoInterval_withPeriodAndEndInstantString_shouldUpdateIntervalCorrectly() {
        // ARRANGE
        // An ISO 8601 interval string defined by a period (P1Y2M) before an end instant.
        // When the chronology is passed as null, the default ISO chronology should be used.
        final String intervalString = "P1Y2M/2004-06-09";

        // The end instant is parsed directly from the string.
        final DateTime expectedEnd = new DateTime("2004-06-09T00:00:00.000");
        // The start instant is calculated by subtracting the period (1 year, 2 months) from the end.
        final DateTime expectedStart = new DateTime("2003-04-09T00:00:00.000");
        final Interval expectedInterval = new Interval(expectedStart, expectedEnd);

        // The initial state of the interval is irrelevant, as setInto() overwrites it completely.
        final MutableInterval intervalToUpdate = new MutableInterval();

        // ACT
        StringConverter.INSTANCE.setInto(intervalToUpdate, intervalString, null);

        // ASSERT
        // The equals() method for AbstractInterval checks start millis, end millis, and chronology,
        // so this single assertion validates the entire state of the interval.
        assertEquals(expectedInterval, intervalToUpdate);
    }
}