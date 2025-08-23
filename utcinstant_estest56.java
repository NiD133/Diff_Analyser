package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that compareTo() returns 0 when an instant is compared to itself.
     * This verifies the reflexivity property of the Comparable contract (x.compareTo(x) == 0).
     */
    @Test
    public void compareTo_shouldReturnZero_whenComparedToItself() {
        // Arrange
        UtcInstant anInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);

        // Act
        int comparisonResult = anInstant.compareTo(anInstant);

        // Assert
        assertEquals("An instant compared to itself should result in 0.", 0, comparisonResult);
    }
}