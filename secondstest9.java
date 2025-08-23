package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void getFieldType_shouldReturnSecondsType() {
        // Arrange
        Seconds seconds = Seconds.seconds(20);
        DurationFieldType expectedType = DurationFieldType.seconds();

        // Act
        DurationFieldType actualType = seconds.getFieldType();

        // Assert
        assertEquals(expectedType, actualType);
    }
}