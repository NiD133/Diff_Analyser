package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.TemporalField;

/**
 * Unit tests for the {@link DayOfYear} class.
 */
public class DayOfYearTest {

    /**
     * Tests that the get() method correctly throws a NullPointerException
     * when passed a null TemporalField, adhering to its contract of not
     * accepting null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void get_shouldThrowNullPointerException_whenFieldIsNull() {
        // Arrange: Create an arbitrary instance of DayOfYear.
        // Any valid instance will suffice for this test.
        DayOfYear dayOfYear = DayOfYear.of(150);

        // Act: Call the get() method with a null argument.
        dayOfYear.get((TemporalField) null);

        // Assert: The test expects a NullPointerException.
        // This is handled declaratively by the 'expected' attribute of the @Test annotation.
    }
}