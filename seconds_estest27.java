package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void dividedBy_whenDividingByPositiveInteger_returnsCorrectQuotient() {
        // Arrange: Create a Seconds object to act as the dividend.
        Seconds initialSeconds = Seconds.seconds(1804);
        int divisor = 1804;

        // Act: Perform the division.
        Seconds result = initialSeconds.dividedBy(divisor);

        // Assert: Verify that the result is the expected quotient.
        int expectedSeconds = 1;
        assertEquals(expectedSeconds, result.getSeconds());
    }
}