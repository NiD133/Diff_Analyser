package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Verifies that getFieldType() returns the correct singleton instance for weeks.
     */
    @Test
    public void getFieldType_shouldReturnWeeksType() {
        // Arrange
        Weeks weeks = Weeks.THREE; // Any Weeks instance will work for this test

        // Act
        DurationFieldType actualFieldType = weeks.getFieldType();

        // Assert
        // The field type for a Weeks object should always be the singleton 'weeks' type.
        // We use assertSame to verify it's the exact same object instance.
        assertSame(DurationFieldType.weeks(), actualFieldType);
    }
}