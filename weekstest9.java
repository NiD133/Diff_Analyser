package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Verifies that getFieldType() consistently returns the 'weeks' duration field type.
     */
    @Test
    public void getFieldType_shouldReturnWeeksType() {
        // Arrange
        Weeks twentyWeeks = Weeks.weeks(20);
        DurationFieldType expectedType = DurationFieldType.weeks();

        // Act
        DurationFieldType actualType = twentyWeeks.getFieldType();

        // Assert
        assertEquals(expectedType, actualType);
    }
}