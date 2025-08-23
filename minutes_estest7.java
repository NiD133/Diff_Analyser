package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the conversion methods of the {@link Minutes} class.
 */
public class MinutesConversionTest {

    @Test
    public void toStandardSeconds_whenZeroMinutes_returnsZeroSeconds() {
        // Arrange
        Minutes zeroMinutes = Minutes.ZERO;
        Seconds expectedSeconds = Seconds.ZERO;

        // Act
        Seconds actualSeconds = zeroMinutes.toStandardSeconds();

        // Assert
        assertEquals(expectedSeconds, actualSeconds);
    }
}