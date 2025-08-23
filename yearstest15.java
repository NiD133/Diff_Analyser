package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Test cases for the Years class, focusing on the plus() method.
 */
public class YearsTest {

    @Test
    public void testPlus_addsYearsCorrectly() {
        // Arrange
        final Years twoYears = Years.years(2);
        final Years threeYears = Years.years(3);
        final Years expected = Years.years(5);

        // Act
        final Years actual = twoYears.plus(threeYears);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testPlus_isImmutable() {
        // Arrange
        final Years twoYears = Years.years(2);
        final Years threeYears = Years.years(3);

        // Act: Call the plus method, but ignore the result.
        twoYears.plus(threeYears);

        // Assert: Verify the original objects were not mutated.
        assertEquals("Original object should be immutable", 2, twoYears.getYears());
        assertEquals("Parameter object should be immutable", 3, threeYears.getYears());
    }

    @Test
    public void testPlus_whenAddingNull_treatsNullAsZero() {
        // Arrange
        final Years oneYear = Years.ONE;

        // Act
        final Years result = oneYear.plus((Years) null);

        // Assert: Adding null should be a no-op and return the same instance.
        assertSame(oneYear, result);
    }

    @Test
    public void testPlus_whenAddingZero_returnsSameInstance() {
        // Arrange
        final Years oneYear = Years.ONE;

        // Act
        final Years result = oneYear.plus(Years.ZERO);

        // Assert: Adding zero should be a no-op and return the same instance.
        assertSame(oneYear, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testPlus_whenResultExceedsMaxValue_throwsArithmeticException() {
        // Act: This operation should cause an integer overflow.
        Years.MAX_VALUE.plus(Years.ONE);
    }
}