package org.threeten.extra;

import java.time.temporal.TemporalField;
import org.junit.Test;

/**
 * This test suite focuses on the range() method of the DayOfYear class.
 */
public class DayOfYearRangeTest {

    /**
     * Tests that calling the range() method with a null argument
     * throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void range_whenFieldIsNull_throwsNullPointerException() {
        // Arrange: Create a DayOfYear instance. Using a fixed value is more
        // deterministic and clearer than using DayOfYear.now().
        DayOfYear dayOfYear = DayOfYear.of(1);

        // Act: Call the range() method with a null argument.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        dayOfYear.range((TemporalField) null);
    }
}