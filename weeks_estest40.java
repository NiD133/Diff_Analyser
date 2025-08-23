package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Weeks} class, focusing on comparison methods.
 */
public class WeeksTest {

    /**
     * Tests that isLessThan() returns false when comparing a positive Weeks
     * instance to null. The documentation specifies that a null comparison
     * is treated as a comparison to a zero-valued period.
     */
    @Test
    public void isLessThan_shouldReturnFalse_whenComparingToNull() {
        // Arrange: A non-zero, positive period of three weeks.
        Weeks threeWeeks = Weeks.THREE;

        // Act & Assert: According to the Javadoc, comparing to null is equivalent
        // to comparing to Weeks.ZERO. Since 3 is not less than 0, the result
        // should be false.
        assertFalse("Weeks.THREE should not be less than null (which is treated as zero)", 
                    threeWeeks.isLessThan(null));
    }
}