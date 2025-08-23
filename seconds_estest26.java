package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void getSeconds_returnsValueUsedInFactory() {
        // Arrange
        final int expectedSeconds = -2530;
        Seconds seconds = Seconds.seconds(expectedSeconds);

        // Act
        int actualSeconds = seconds.getSeconds();

        // Assert
        assertEquals(expectedSeconds, actualSeconds);
    }
}