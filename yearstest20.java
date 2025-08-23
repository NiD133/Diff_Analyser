package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class, focusing on the negated() method.
 */
public class YearsTest {

    /**
     * Tests that negating a Years object returns a new instance with the opposite value
     * and does not modify the original object.
     */
    @Test
    public void negated_returnsNewInstanceWithOppositeSign_andPreservesOriginal() {
        // Arrange: Create an initial Years instance.
        final Years initialYears = Years.years(12);

        // Act: Negate the initial instance.
        final Years negatedYears = initialYears.negated();

        // Assert:
        // 1. The new instance should have the correct, negated value.
        assertEquals("The negated value should be -12.", -12, negatedYears.getYears());
        
        // 2. The original instance should remain unchanged, confirming immutability.
        assertEquals("The original value should still be 12.", 12, initialYears.getYears());
    }

    /**
     * Tests that attempting to negate Years.MIN_VALUE throws an ArithmeticException,
     * as negating the minimum integer value causes an overflow.
     */
    @Test(expected = ArithmeticException.class)
    public void negated_whenValueIsMinValue_throwsArithmeticException() {
        // Act: Attempt to negate the minimum possible value.
        // This is expected to throw an ArithmeticException due to integer overflow.
        Years.MIN_VALUE.negated();
    }
}