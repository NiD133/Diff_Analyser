package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * This test suite has been improved to enhance readability and maintainability.
 * The original test was brittle due to its reliance on the system clock, making it fail
 * on any day other than the one it was generated on.
 *
 * The key improvements are:
 * 1.  **Descriptive Naming:** Test methods and variables now have names that clearly
 *     describe their purpose.
 * 2.  **Deterministic Behavior:** The test now uses a fixed `Clock` to ensure that
 *     `DayOfYear.now()` returns a predictable result, making the test stable and
 *     independent of the execution date.
 * 3.  **Clear Arrange-Act-Assert Pattern:** The test logic is structured to be
 *     easily understood, with clear separation between setup, execution, and verification.
 * 4.  **Elimination of Magic Numbers:** The expected value (46) is now explained by
 *     the setup, where the clock is explicitly set to February 15th, the 46th day of a non-leap year.
 */
public class DayOfYearTest {

    /**
     * Tests that getValue() returns the correct day-of-year value when the DayOfYear
     * is created from a fixed clock.
     */
    @Test
    public void getValue_returnsCorrectDayOfYear_whenUsingFixedClock() {
        // Arrange: Set up a fixed clock for a specific date.
        // February 15th is the 46th day of a non-leap year (31 days in Jan + 15 days in Feb).
        final int expectedDayOfYear = 46;
        LocalDate date = LocalDate.of(2023, 2, 15); // A non-leap year
        Instant fixedInstant = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        // Act: Create a DayOfYear instance using the fixed clock and get its value.
        DayOfYear dayOfYear = DayOfYear.now(fixedClock);
        int actualDayOfYear = dayOfYear.getValue();

        // Assert: Verify that the retrieved value matches the expected day of the year.
        assertEquals(expectedDayOfYear, actualDayOfYear);
    }
}