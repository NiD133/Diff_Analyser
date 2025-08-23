package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import java.time.Clock;
import java.time.temporal.TemporalAccessor;

// Unused imports from the original test have been removed for clarity.

public class DayOfMonth_ESTestTest39 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that {@code DayOfMonth.from()} returns the same cached instance
     * when passed an existing {@code DayOfMonth} object.
     * <p>
     * The {@code DayOfMonth} class pre-allocates and caches instances for all 31 possible days.
     * This test verifies that the {@code from()} factory method is optimized to return the
     * existing instance directly, rather than creating a new one.
     */
    @Test
    public void from_whenCalledWithDayOfMonthInstance_shouldReturnSameInstance() {
        // Arrange: Directly create a DayOfMonth instance. This is clearer than
        // using a mock clock, as it removes any ambiguity about the initial value.
        DayOfMonth originalDayOfMonth = DayOfMonth.of(14);

        // Act: Call the from() factory method, passing the existing instance.
        DayOfMonth resultFromFactory = DayOfMonth.from(originalDayOfMonth);

        // Assert: The factory method should return the exact same object, not just
        // an equal one. assertSame() checks for reference equality (obj1 == obj2).
        assertSame(
            "DayOfMonth.from() should return the same cached instance for a DayOfMonth input",
            originalDayOfMonth,
            resultFromFactory
        );
    }
}