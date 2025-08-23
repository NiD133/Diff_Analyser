package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link TaiInstant} class, focusing on its comparison logic.
 */
public class TaiInstantTest {

    /**
     * Tests that comparing a TaiInstant to itself returns 0.
     * This verifies a fundamental property of the Comparable contract,
     * where an object must be equal to itself.
     */
    @Test
    public void compareTo_whenComparedToItself_returnsZero() {
        // Arrange: Create an instance of TaiInstant representing the TAI epoch.
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 0L);

        // Act: Compare the instant to itself.
        int result = instant.compareTo(instant);

        // Assert: The result should be 0, indicating equality.
        assertEquals("An instant compared to itself should be equal (return 0).", 0, result);
    }
}