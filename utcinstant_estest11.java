package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for {@link UtcInstant}.
 * This class focuses on the behavior of the withNanoOfDay method.
 */
public class UtcInstantTest {

    /**
     * Tests that withNanoOfDay returns a new UtcInstant with the nano-of-day updated,
     * while leaving the Modified Julian Day unchanged. It also verifies the immutability
     * of the original instance.
     */
    @Test
    public void withNanoOfDay_shouldReturnNewInstanceWithUpdatedNanosAndSameMjd() {
        // Arrange
        final long initialMjd = -2985L;
        final long initialNanoOfDay = 0L;
        final long newNanoOfDay = 1000L;
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanoOfDay);

        // Act
        UtcInstant updatedInstant = initialInstant.withNanoOfDay(newNanoOfDay);

        // Assert
        // 1. Verify the state of the new instance
        assertEquals("Modified Julian Day should be unchanged in the new instance.",
                initialMjd, updatedInstant.getModifiedJulianDay());
        assertEquals("Nano-of-day should be updated in the new instance.",
                newNanoOfDay, updatedInstant.getNanoOfDay());

        // 2. Verify that the original instance is immutable
        assertNotSame("A new instance should be returned.", initialInstant, updatedInstant);
        assertEquals("Original instance's Modified Julian Day should not change.",
                initialMjd, initialInstant.getModifiedJulianDay());
        assertEquals("Original instance's nano-of-day should not change.",
                initialNanoOfDay, initialInstant.getNanoOfDay());
    }
}