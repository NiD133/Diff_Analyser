package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

/**
 * Tests for {@link DayOfMonth#now(ZoneId)}.
 */
class DayOfMonthNowTest {

    /**
     * Tests that DayOfMonth.now(ZoneId) is consistent with LocalDate.now(ZoneId).
     * <p>
     * This test relies on the system clock and can be flaky if run precisely at midnight
     * in the specified time zone. The two {@code now()} calls (one for {@code DayOfMonth} and one
     * for {@code LocalDate}) could happen on different days, causing a race condition.
     * <p>
     * The {@link RetryingTest} annotation mitigates this rare possibility by re-running
     * the test if it fails. A successful run confirms the logic is correct for a stable
     * moment in time.
     */
    @Test
    @RetryingTest(value = 100, name = "Retry to avoid midnight race condition")
    void now_withZoneId_returnsCurrentDayOfMonthForThatZone() {
        // Arrange
        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");

        // Act
        // These two calls should ideally execute on the same day.
        DayOfMonth actualDayOfMonth = DayOfMonth.now(tokyoZone);
        int expectedDayOfMonth = LocalDate.now(tokyoZone).getDayOfMonth();

        // Assert
        assertEquals(expectedDayOfMonth, actualDayOfMonth.getValue(),
                "DayOfMonth.now(zone) should be consistent with LocalDate.now(zone)");
    }
}