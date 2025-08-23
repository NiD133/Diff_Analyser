package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link UtcInstant} class, focusing on comparison methods.
 */
public class UtcInstantTest {

    /**
     * Tests that an instant is not considered to be "before" itself.
     * The comparison should be reflexive, where an object is equal to, but not
     * before or after, itself.
     */
    @Test
    public void isBefore_whenComparedToSelf_shouldReturnFalse() {
        // Arrange
        // The original test used Long.MAX_VALUE for the Modified Julian Day,
        // which represents a valid boundary condition test. We retain this to
        // ensure the test covers extreme values.
        long maxModifiedJulianDay = Long.MAX_VALUE;
        long nanoOfDay = 73_281_320_003_515L; // An arbitrary, valid nano-of-day value.
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(maxModifiedJulianDay, nanoOfDay);

        // Act
        boolean isBefore = instant.isBefore(instant);

        // Assert
        assertFalse("An instant should never be considered 'before' itself.", isBefore);
    }
}