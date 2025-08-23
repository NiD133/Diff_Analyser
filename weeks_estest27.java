package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Verifies that Weeks.weeksBetween() throws an IllegalArgumentException when
     * the start and end partials have different fields. The method requires that
     * both partials have the same set of fields to be comparable.
     */
    @Test(expected = IllegalArgumentException.class)
    public void weeksBetween_throwsExceptionForPartialsWithDifferentFields() {
        // Arrange: Create two ReadablePartial objects with different sets of fields.
        // A LocalDate contains (year, month, day), while a YearMonth only contains (year, month).
        ReadablePartial startPartial = new LocalDate(2023, 5, 10);
        ReadablePartial endPartial = new YearMonth(2023, 6);

        // Act: Attempt to calculate the weeks between the two incompatible partials.
        // This is expected to throw an IllegalArgumentException.
        Weeks.weeksBetween(startPartial, endPartial);

        // Assert: The test passes if the expected IllegalArgumentException is thrown,
        // which is handled by the @Test(expected = ...) annotation.
    }
}