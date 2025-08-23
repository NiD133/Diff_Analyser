package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    @Test
    public void getCalendarType_shouldReturnNull() {
        // The Javadoc for getCalendarType() states it returns null because there is
        // no standard Unicode LDML identifier for this specific calendar system.
        // This test verifies that behavior.

        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Act
        String calendarType = chronology.getCalendarType();

        // Assert
        assertNull("The calendar type is expected to be null.", calendarType);
    }
}