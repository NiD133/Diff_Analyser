package org.threeten.extra.chrono;

import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Tests for {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void getCalendarType_shouldReturnNull() {
        // The Javadoc for InternationalFixedChronology#getCalendarType states that it returns null
        // because the Unicode LDML specification does not define an identifier for this calendar system.
        // This test verifies that expected behavior.

        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;

        // Act
        String calendarType = chronology.getCalendarType();

        // Assert
        assertNull("The calendar type should be null as it is not defined in the LDML specification.", calendarType);
    }
}