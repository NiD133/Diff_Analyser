package org.joda.time.chrono;

import static org.junit.Assert.assertFalse;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Unit tests for {@link CopticChronology}.
 */
public class CopticChronologyTest {

    /**
     * Tests that two CopticChronology instances are not considered equal if they are
     * configured with a different minimum number of days in the first week.
     */
    @Test
    public void chronologiesWithDifferentMinDaysInFirstWeekAreNotEqual() {
        // Arrange: Create two CopticChronology instances for the same time zone
        // but with different values for 'minimum days in the first week'.
        // The default getInstance() uses a value of 4.
        CopticChronology defaultChronology = CopticChronology.getInstance();
        CopticChronology chronologyWithMinDays1 = CopticChronology.getInstance(defaultChronology.getZone(), 1);

        // Act & Assert: The two instances should not be equal, as the equals()
        // implementation checks for this specific property.
        assertFalse(chronologyWithMinDays1.equals(defaultChronology));
    }
}