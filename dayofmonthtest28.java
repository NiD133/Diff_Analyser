package org.threeten.extra;

import static org.junit.Assert.assertFalse;
import java.time.ZoneOffset;
import org.junit.Test;

/**
 * A more descriptive class name, like DayOfMonthEqualsTest, would be preferable.
 * The original name is kept here to match the user's request.
 */
public class DayOfMonth_ESTestTest28 {

    /**
     * Tests that the equals() method returns false when comparing a DayOfMonth
     * instance with an object of a different and incompatible type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedAgainstDifferentType() {
        // Arrange: Create a DayOfMonth instance and an object of a different type.
        // Using DayOfMonth.of() makes the test deterministic and self-contained.
        DayOfMonth dayOfMonth = DayOfMonth.of(14);
        Object otherObject = ZoneOffset.MAX;

        // Act: Compare the DayOfMonth object with the incompatible object.
        boolean areEqual = dayOfMonth.equals(otherObject);

        // Assert: The result of the comparison must be false.
        assertFalse("DayOfMonth should not be equal to an object of a different type.", areEqual);
    }
}