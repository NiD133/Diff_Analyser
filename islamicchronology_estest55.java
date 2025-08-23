package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the equals() method in {@link IslamicChronology}.
 */
public class IslamicChronology_ESTestTest55 {

    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        Object otherObject = new Object();

        // Act
        boolean isEqual = islamicChronology.equals(otherObject);

        // Assert
        assertFalse(isEqual);
    }
}