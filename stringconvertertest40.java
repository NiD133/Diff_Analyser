package org.joda.time.convert;

import org.joda.time.MutableInterval;
import org.junit.Test;

/**
 * Tests the behavior of {@link StringConverter#setInto(ReadWritableInterval, Object, Chronology)}
 * when handling invalid string formats for intervals.
 */
public class StringConverterInvalidIntervalStringTest {

    /**
     * Verifies that setInto() throws an IllegalArgumentException when the input string
     * for an interval is malformed.
     *
     * The ISO-8601 standard for intervals requires a start and end instant, or a
     * start/end instant combined with a duration/period. A string like "/P1Y", which
     * only contains a period without a reference instant, is not a valid representation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setIntoInterval_withPeriodOnlyString_throwsIllegalArgumentException() {
        // Arrange: Define the invalid input and a target interval object.
        // The string "/P1Y" is invalid because it only contains a period (P1Y)
        // without a start or end instant to anchor it.
        String invalidIntervalString = "/P1Y";
        MutableInterval interval = new MutableInterval(0L, 1000L);

        // Act: Attempt to update the interval's value from the invalid string.
        // The StringConverter is a singleton, accessed via its INSTANCE field.
        StringConverter.INSTANCE.setInto(interval, invalidIntervalString, null);

        // Assert: The test will automatically pass if an IllegalArgumentException is thrown,
        // as specified by the @Test(expected=...) annotation. It will fail otherwise.
    }
}