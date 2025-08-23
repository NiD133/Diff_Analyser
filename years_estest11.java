package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that multiplying a Years instance by a negative number
     * correctly calculates the product.
     */
    @Test
    public void multipliedBy_withNegativeScalar_calculatesCorrectProduct() {
        // Arrange
        final int baseValue = -1249;
        final int multiplier = -1249;
        final Years years = Years.years(baseValue);

        // Act
        final Years result = years.multipliedBy(multiplier);

        // Assert
        final int expectedValue = baseValue * multiplier; // -1249 * -1249 = 1560001
        assertEquals(expectedValue, result.getYears());
    }
}