package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link UtcInstant} class.
 */
public class UtcInstantTest {

    /**
     * Verifies that isAfter() returns false when an instant is compared to itself,
     * which is a fundamental property for any consistent comparison method.
     */
    @Test
    public void isAfter_whenComparedToSelf_returnsFalse() {
        // Arrange: Create an arbitrary instant. The specific value does not matter for this test.
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(0L, 0L);

        // Act & Assert: An instant should not be considered "after" itself.
        assertFalse("An instant should not be considered after itself.", instant.isAfter(instant));
    }
}