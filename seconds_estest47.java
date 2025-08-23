package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that the minus() method treats a null argument as zero, resulting in no change.
     * The Javadoc for minus(Seconds) specifies that a null input is equivalent to subtracting zero.
     */
    @Test
    public void minus_whenArgumentIsNull_doesNotChangeValue() {
        // Arrange: Start with a known Seconds value.
        Seconds initialSeconds = Seconds.TWO;

        // Act: Subtract a null Seconds object.
        Seconds result = initialSeconds.minus((Seconds) null);

        // Assert: The resulting value should be unchanged.
        assertEquals(initialSeconds, result);
    }
}