package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that a Seconds object created via the factory method
     * holds the correct value and has the correct field type.
     */
    @Test
    public void secondsFactory_shouldCreateInstanceWithCorrectState() {
        // Arrange
        final int expectedValue = 2;
        Seconds twoSeconds = Seconds.seconds(expectedValue);

        // Act & Assert
        // Verify that getSeconds() returns the value used at creation.
        assertEquals("The number of seconds should match the value provided at creation.",
                expectedValue, twoSeconds.getSeconds());

        // Verify that the field type is correctly identified as 'seconds'.
        assertEquals("The field type should be 'seconds'.",
                DurationFieldType.seconds(), twoSeconds.getFieldType());
    }
}