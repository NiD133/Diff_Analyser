package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Years} class, focusing on comparison logic.
 */
public class YearsTest {

    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingToSmallerValue() {
        // Arrange
        Years threeYears = Years.THREE;
        Years minusOneYear = Years.years(-1);

        // Act
        boolean isGreater = threeYears.isGreaterThan(minusOneYear);

        // Assert
        assertTrue("3 years should be considered greater than -1 years", isGreater);
    }
}