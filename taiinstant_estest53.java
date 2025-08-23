package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link TaiInstant} class, focusing on method contracts and behavior.
 */
public class TaiInstantTest {

    /**
     * Tests that withNano() throws an IllegalArgumentException when the provided
     * nano-of-second value is greater than the maximum allowed value.
     */
    @Test
    public void withNano_shouldThrowException_whenNanoValueIsTooLarge() {
        // Arrange: Create a base instant and define an invalid nano-of-second value
        // that is just above the maximum allowed limit.
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(0, 0);
        int invalidNanoValue = 1_000_000_000; // Max is 999,999,999

        // Act & Assert: Verify that calling withNano with the invalid value throws
        // an IllegalArgumentException with the correct message.
        try {
            baseInstant.withNano(invalidNanoValue);
            fail("Expected an IllegalArgumentException to be thrown for a nano-of-second value greater than 999,999,999.");
        } catch (IllegalArgumentException e) {
            // Verify the exception message is as expected.
            assertEquals("NanoOfSecond must be from 0 to 999,999,999", e.getMessage());
        }
    }

    /**
     * Tests that withNano() throws an IllegalArgumentException when the provided
     * nano-of-second value is negative.
     */
    @Test
    public void withNano_shouldThrowException_whenNanoValueIsNegative() {
        // Arrange: Create a base instant and define a negative, invalid nano-of-second value.
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(0, 0);
        int invalidNanoValue = -1; // Min is 0

        // Act & Assert: Verify that calling withNano with the invalid value throws
        // an IllegalArgumentException with the correct message.
        try {
            baseInstant.withNano(invalidNanoValue);
            fail("Expected an IllegalArgumentException to be thrown for a negative nano-of-second value.");
        } catch (IllegalArgumentException e) {
            // Verify the exception message is as expected.
            assertEquals("NanoOfSecond must be from 0 to 999,999,999", e.getMessage());
        }
    }
}