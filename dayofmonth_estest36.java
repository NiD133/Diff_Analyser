package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.TemporalField;

// Note: The original class name and inheritance are kept to match the provided context.
// In a real-world scenario, this class would likely be named DayOfMonthTest.
public class DayOfMonth_ESTestTest36 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that calling getLong() with a null TemporalField throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void getLong_whenFieldIsNull_throwsNullPointerException() {
        // Arrange: Create a sample DayOfMonth instance. Any valid day will suffice.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act & Assert: Call getLong with a null argument.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        dayOfMonth.getLong((TemporalField) null);
    }
}