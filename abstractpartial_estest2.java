package org.joda.time.base;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Test class for the comparison methods in {@link AbstractPartial}.
 * <p>
 * This test uses {@link LocalDateTime} as a concrete implementation of
 * a readable partial to test the behavior of methods like {@code isAfter()}.
 */
public class AbstractPartialTest {

    /**
     * Tests that isAfter() returns false when comparing a partial to another partial
     * that represents a later point in time.
     */
    @Test
    public void isAfter_shouldReturnFalse_whenPartialIsEarlierThanComparator() {
        // Arrange: Create a specific date-time (noon) and a time that is clearly earlier (10 AM).
        // Using fixed values instead of LocalDateTime.now() makes the test deterministic.
        LocalDateTime noonOnSpecificDay = new LocalDateTime(2023, 10, 26, 12, 0, 0);
        LocalTime tenAm = new LocalTime(10, 0, 0);

        // Act: Create a new LocalDateTime by replacing the time part of the original
        // with the earlier time. This results in 10 AM on the same day.
        LocalDateTime tenAmOnSpecificDay = noonOnSpecificDay.withFields(tenAm);

        // Assert: Verify that the 10 AM partial is not considered "after" the 12 PM partial.
        boolean isAfter = tenAmOnSpecificDay.isAfter(noonOnSpecificDay);
        assertFalse("10:00 should not be after 12:00 on the same day", isAfter);

        // For additional clarity, we can also assert the state of our modified object.
        assertEquals(new LocalDateTime(2023, 10, 26, 10, 0, 0), tenAmOnSpecificDay);
    }
}