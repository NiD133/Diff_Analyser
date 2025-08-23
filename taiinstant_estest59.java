package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link TaiInstant} class.
 */
public class TaiInstantTest {

    /**
     * Tests the string representation of a TaiInstant.
     * The format should be {seconds}.{nanos}s(TAI), with nanos padded to 9 digits.
     */
    @Test
    public void testToString_withPositiveSecondsAndZeroNanos() {
        // Arrange: Create an instant for 1 TAI second and 0 nanoseconds.
        TaiInstant instant = TaiInstant.ofTaiSeconds(1L, 0L);
        String expectedString = "1.000000000s(TAI)";

        // Act: Convert the instant to its string representation.
        String actualString = instant.toString();

        // Assert: The actual string should match the expected format.
        assertEquals(expectedString, actualString);
    }
}