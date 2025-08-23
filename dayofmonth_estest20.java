package org.threeten.extra;

import org.junit.Test;

/**
 * Tests for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    @Test(expected = NullPointerException.class)
    public void compareTo_withNullArgument_throwsNullPointerException() {
        // Arrange
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act
        dayOfMonth.compareTo(null);

        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
    }
}