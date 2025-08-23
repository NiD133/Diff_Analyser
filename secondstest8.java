package org.joda.time;

import junit.framework.TestCase;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest extends TestCase {

    /**
     * Tests that getSeconds() returns the same value used to create the instance.
     */
    public void testGetSeconds_returnsValueFromConstructor() {
        // Arrange
        final int expectedSeconds = 20;
        Seconds secondsInstance = Seconds.seconds(expectedSeconds);

        // Act
        int actualSeconds = secondsInstance.getSeconds();

        // Assert
        assertEquals(expectedSeconds, actualSeconds);
    }
}