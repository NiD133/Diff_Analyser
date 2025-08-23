package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that {@link Minutes#standardMinutesIn(ReadablePeriod)} returns zero
     * when the input period is null, as specified by its Javadoc.
     */
    @Test
    public void standardMinutesIn_withNullPeriod_returnsZero() {
        // Act: Call the method under test with a null input.
        Minutes result = Minutes.standardMinutesIn(null);

        // Assert: The result should be a Minutes object representing zero.
        assertEquals(Minutes.ZERO, result);
    }
}