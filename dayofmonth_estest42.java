package org.threeten.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.ZoneId;
import org.junit.Test;

/**
 * Tests for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    @Test
    public void now_givenNullZoneId_shouldThrowNullPointerException() {
        // The Javadoc for now(ZoneId) specifies that the zone parameter must not be null.
        // This test verifies that a NullPointerException is thrown as expected.
        try {
            DayOfMonth.now((ZoneId) null);
            fail("DayOfMonth.now(null) should have thrown a NullPointerException.");
        } catch (NullPointerException e) {
            // The underlying implementation uses Objects.requireNonNull(zone, "zone"),
            // so we can assert the specific message for a more robust test.
            assertEquals("zone", e.getMessage());
        }
    }
}