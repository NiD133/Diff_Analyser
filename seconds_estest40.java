package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Seconds} class.
 * This test focuses on the isLessThan() method.
 */
public class SecondsTest {

    @Test
    public void isLessThan_shouldReturnTrue_whenNegativeSecondsIsComparedToNull() {
        // Arrange
        // According to the Javadoc, comparing with a null Seconds object
        // is equivalent to comparing with Seconds.ZERO.
        Seconds negativeSeconds = Seconds.seconds(-1300);

        // Act
        // Perform the comparison against a null value.
        boolean isLess = negativeSeconds.isLessThan(null);

        // Assert
        assertTrue("A negative Seconds value should be considered less than null (which is treated as zero).", isLess);
    }
}