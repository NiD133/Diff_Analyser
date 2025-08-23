package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that dividing a zero-week period by one results in a zero-week period.
     * This test specifically obtains the initial zero-week period by calling
     * {@code Weeks.standardWeeksIn(null)}, which is documented to return zero.
     */
    @Test
    public void dividingZeroWeeksByOneShouldReturnZeroWeeks() {
        // Arrange
        // The Javadoc for standardWeeksIn states that a null input returns zero weeks.
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        int divisor = 1;

        // Act
        Weeks result = zeroWeeks.dividedBy(divisor);

        // Assert
        assertEquals("Dividing zero by one should result in zero.", Weeks.ZERO, result);
    }
}