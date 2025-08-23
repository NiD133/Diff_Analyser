package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.TemporalField;

/**
 * Tests for the get(TemporalField) method of {@link DayOfMonth}.
 */
public class DayOfMonthGetTest {

    /**
     * Tests that calling get() with a null field throws a NullPointerException,
     * as per the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void get_whenFieldIsNull_throwsNullPointerException() {
        // Arrange: Create an arbitrary DayOfMonth instance. 
        // The specific value (15) is not important for this test.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Call the get() method with a null argument.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        dayOfMonth.get((TemporalField) null);
    }
}