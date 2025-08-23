package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DateTimeZone;

/**
 * This test class focuses on the equality logic of the CopticChronology class.
 */
public class CopticChronologyEqualityTest {

    /**
     * A custom value for the 'minimum days in the first week' setting, different from the default of 4.
     */
    private static final int CUSTOM_MIN_DAYS_IN_FIRST_WEEK = 1;

    /**
     * Tests that two CopticChronology instances are not considered equal if they are
     * configured with different values for 'minimum days in the first week'.
     *
     * <p>The default CopticChronology uses a value of 4 for this setting. This test
     * creates a second instance with a custom value and asserts they are not equal,
     * verifying that this configuration parameter is correctly handled in the equals() method.</p>
     */
    @Test
    public void chronologiesWithDifferentMinDaysInFirstWeekShouldNotBeEqual() {
        // Arrange: Create a default CopticChronology instance and one with a custom
        // 'minDaysInFirstWeek' value. The default is 4.
        CopticChronology defaultChronology = CopticChronology.getInstance();
        CopticChronology chronologyWithCustomMinDays = CopticChronology.getInstance(
            DateTimeZone.getDefault(),
            CUSTOM_MIN_DAYS_IN_FIRST_WEEK
        );

        // Act & Assert: Verify that the two instances are not equal.
        // The equals() method should account for the difference in 'minDaysInFirstWeek'.
        assertNotEquals(
            "Chronologies should not be equal if their 'minDaysInFirstWeek' differ.",
            defaultChronology,
            chronologyWithCustomMinDays
        );
    }
}