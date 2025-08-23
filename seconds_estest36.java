package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void secondsFactoryMethod_createsInstanceWithCorrectValue() {
        // Arrange
        int expectedSeconds = 1;

        // Act
        Seconds result = Seconds.seconds(expectedSeconds);

        // Assert
        assertEquals(expectedSeconds, result.getSeconds());
    }
}