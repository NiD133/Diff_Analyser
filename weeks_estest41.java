package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that calling {@link Weeks#standardWeeksIn(ReadablePeriod)} with a null input
     * correctly returns a {@code Weeks} object representing zero.
     */
    @Test
    public void standardWeeksIn_givenNullPeriod_returnsZeroWeeks() {
        // Arrange: The Javadoc for standardWeeksIn states that a null input
        // should be treated as a zero-length period.
        ReadablePeriod nullPeriod = null;

        // Act: Create a Weeks instance from the null period.
        Weeks zeroWeeks = Weeks.standardWeeksIn(nullPeriod);

        // Assert: Verify that the resulting object represents zero weeks.
        assertEquals("standardWeeksIn(null) should result in zero weeks.", 0, zeroWeeks.getWeeks());

        // Further Assert: A value cannot be less than itself. This is a good sanity check
        // on the state of the returned object and the isLessThan method.
        assertFalse("A Weeks instance should not be less than itself.", zeroWeeks.isLessThan(zeroWeeks));
    }
}