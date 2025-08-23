package org.threeten.extra;

import org.junit.Test;

/**
 * Tests for {@link DayOfYear}.
 */
public class DayOfYearTest {

    /**
     * Tests that getLong() throws a NullPointerException when the field is null.
     */
    @Test(expected = NullPointerException.class)
    public void getLong_whenFieldIsNull_throwsNullPointerException() {
        // Arrange: Create an arbitrary DayOfYear instance.
        // The specific value does not matter for this test.
        DayOfYear dayOfYear = DayOfYear.of(150);

        // Act: Call the method under test with a null argument.
        // The @Test(expected) annotation will assert that an exception is thrown.
        dayOfYear.getLong(null);
    }
}