package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that calling {@code standardWeeksIn} with a null {@code ReadablePeriod}
     * results in a zero-week period, as specified by the method's contract.
     */
    @Test
    public void standardWeeksIn_withNullPeriod_returnsZeroWeeks() {
        // Arrange: According to the Javadoc, a null input period should be treated as zero.
        ReadablePeriod nullPeriod = null;

        // Act: Create a Weeks instance from the null period.
        Weeks result = Weeks.standardWeeksIn(nullPeriod);

        // Assert: The resulting period should be equivalent to the constant Weeks.ZERO.
        assertEquals(Weeks.ZERO, result);
    }
}