package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void isLessThan_shouldReturnFalse_whenComparingLargerToSmaller() {
        // Arrange
        Years largerYears = Years.MAX_VALUE;
        Years smallerYears = Years.THREE;

        // Act & Assert
        assertFalse("MAX_VALUE years should not be considered less than THREE years",
                largerYears.isLessThan(smallerYears));
    }
}