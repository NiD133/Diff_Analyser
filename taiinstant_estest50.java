package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test class for the {@link TaiInstant#isAfter(TaiInstant)} method.
 */
public class TaiInstantIsAfterTest {

    /**
     * Verifies that an instant is not considered to be after itself.
     * The isAfter() method should return false when an instant is compared to an identical instant.
     */
    @Test
    public void isAfter_whenComparedToSelf_returnsFalse() {
        // Arrange: Create a TaiInstant instance. The specific value is arbitrary.
        TaiInstant instant = TaiInstant.ofTaiSeconds(37L, 37L);

        // Act: Check if the instant is after itself.
        boolean isAfter = instant.isAfter(instant);

        // Assert: The result should be false.
        assertFalse("An instant should never be considered after itself.", isAfter);
    }
}