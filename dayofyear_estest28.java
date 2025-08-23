package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * This class contains an improved version of a test for the {@link DayOfYear} class.
 * The original test was automatically generated and has been refactored for clarity
 * and maintainability.
 */
// The original test extended a scaffolding class, which might be necessary
// for the test environment setup. It is kept for compatibility.
public class DayOfYear_ESTestTest28 extends DayOfYear_ESTest_scaffolding {

    /**
     * Verifies that the equals() method returns false when comparing two
     * DayOfYear instances that represent different days.
     *
     * <p>The original test was brittle because it depended on the system clock and
     * specific time zones. This revised version is deterministic, using explicit,
     * constant values to ensure the test is reliable and easy to understand.
     */
    @Test
    public void equals_shouldReturnFalse_forDifferentDayOfYearInstances() {
        // Arrange: Create two distinct DayOfYear instances with different values.
        DayOfYear day100 = DayOfYear.of(100);
        DayOfYear day200 = DayOfYear.of(200);

        // Act & Assert: Verify that the two instances are not considered equal.
        // Using assertNotEquals is more direct and readable than assertFalse(a.equals(b)).
        assertNotEquals(day100, day200);
    }
}