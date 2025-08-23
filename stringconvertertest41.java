package org.joda.time.convert;

import org.joda.time.MutableInterval;
import org.junit.Test;

/**
 * Tests for {@link StringConverter#setInto(ReadWritableInterval, Object, Chronology)}.
 */
public class StringConverterSetIntoIntervalTest {

    /**
     * Tests that setInto() throws an IllegalArgumentException when provided with an
     * interval string defined by two periods (e.g., "P1Y/P2Y").
     *
     * The setInto method for an interval expects a string representing a start and end
     * instant, not two durations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setInto_throwsException_forIntervalStringWithTwoPeriods() {
        // Arrange
        final String intervalStringWithTwoPeriods = "P1Y/P2Y";
        MutableInterval interval = new MutableInterval(0L, 1000L);
        StringConverter converter = StringConverter.INSTANCE;

        // Act: This call is expected to throw an exception.
        converter.setInto(interval, intervalStringWithTwoPeriods, null);

        // Assert: The @Test(expected) annotation handles the exception assertion.
        // The test fails if an IllegalArgumentException is not thrown.
    }
}