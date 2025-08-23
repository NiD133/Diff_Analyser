package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that getFieldType() consistently returns the 'minutes' duration field type,
     * regardless of the specific Minutes value.
     */
    @Test
    public void testGetFieldType_shouldReturnMinutesType() {
        // Arrange: Create an instance of the Minutes class.
        // The specific value does not matter for this test.
        Minutes testMinutes = Minutes.ONE;

        // Act: Call the method under test.
        DurationFieldType fieldType = testMinutes.getFieldType();

        // Assert: Verify that the returned type is the expected 'minutes' type.
        assertEquals(DurationFieldType.minutes(), fieldType);
    }
}