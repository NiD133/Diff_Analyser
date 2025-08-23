package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the TaiInstant class, focusing on comparison methods.
 */
public class TaiInstantTest {

    /**
     * Tests that an instant is not considered to be before itself.
     * The isBefore() method should return false when an instant is compared to the same instant.
     */
    @Test
    public void isBefore_shouldReturnFalse_whenComparingAnInstantToItself() {
        // Arrange: Create a TaiInstant instance. The specific value is not important for this test.
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 0L);

        // Act: Check if the instant is before itself.
        boolean result = instant.isBefore(instant);

        // Assert: The result must be false, as an instant cannot be before itself.
        assertFalse("An instant should not be considered 'before' itself.", result);
    }
}