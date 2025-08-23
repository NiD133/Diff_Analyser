package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for the {@link DayOfYear#toString()} method.
 */
public class DayOfYearToStringTest {

    /**
     * Tests that the toString() method returns a string in the format "DayOfYear:NNN".
     */
    @Test
    public void toString_returnsCorrectlyFormattedString() {
        // Arrange
        int dayValue = 46;
        DayOfYear dayOfYear = DayOfYear.of(dayValue);
        String expectedString = "DayOfYear:46";

        // Act
        String actualString = dayOfYear.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}