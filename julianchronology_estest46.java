package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link JulianChronology} class.
 */
public class JulianChronologyTest {

    @Test
    public void getCalendarType_shouldReturnCorrectIdentifier() {
        // Arrange
        // The JulianChronology is a singleton, so we use its public INSTANCE field.
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        String expectedCalendarType = "julian";

        // Act
        String actualCalendarType = julianChronology.getCalendarType();

        // Assert
        assertEquals("The calendar type should be 'julian'", expectedCalendarType, actualCalendarType);
    }
}