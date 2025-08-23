package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that adding zero to a Weeks instance does not change its value.
     * This test specifically starts with a Weeks object created by parsing a null string,
     * which is defined to result in zero weeks.
     */
    @Test
    public void testPlus_addingZeroToWeeksFromNull_returnsZeroWeeks() {
        // Arrange
        // According to the Javadoc, parseWeeks(null) returns a Weeks object representing zero.
        Weeks zeroWeeks = Weeks.parseWeeks(null);

        // Act
        // The operation under test: adding zero weeks.
        Weeks result = zeroWeeks.plus(0);

        // Assert
        // The result should be equal to the canonical ZERO instance.
        assertEquals("The number of weeks should be 0", 0, result.getWeeks());
        assertSame("Adding 0 should return the canonical ZERO instance", Weeks.ZERO, result);
    }
}