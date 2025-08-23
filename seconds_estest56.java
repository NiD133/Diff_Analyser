package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that the factory method {@code Seconds.seconds(int)} creates an instance
     * holding the correct value, which can be retrieved via {@code getSeconds()}.
     */
    @Test
    public void secondsFactory_createsInstanceWithCorrectValue() {
        // Arrange
        final int expectedSeconds = 3;

        // Act
        Seconds seconds = Seconds.seconds(expectedSeconds);

        // Assert
        assertEquals(expectedSeconds, seconds.getSeconds());
    }
}