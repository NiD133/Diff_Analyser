package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link TaiInstant}.
 */
public class TaiInstantTest {

    @Test
    public void withNano_whenNanoIsOutOfUpperBound_throwsIllegalArgumentException() {
        // Arrange: Create a base TaiInstant and define an invalid nano-of-second value.
        // The valid range for nano-of-second is 0 to 999,999,999.
        // We test the upper boundary by using 1,000,000,000.
        TaiInstant instant = TaiInstant.ofTaiSeconds(50L, 50L);
        int invalidNanoOfSecond = 1_000_000_000;
        String expectedMessage = "NanoOfSecond must be from 0 to 999,999,999";

        // Act & Assert: Attempting to set an out-of-range nano value should fail.
        try {
            instant.withNano(invalidNanoOfSecond);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException ex) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals(expectedMessage, ex.getMessage());
        }
    }
}