package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * Unit tests for the {@link Weeks#plus(Weeks)} method.
 */
public class WeeksTest {

    @Test
    public void plus_addsWeeksCorrectly() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);
        final Weeks threeWeeks = Weeks.weeks(3);

        // Act
        final Weeks sum = twoWeeks.plus(threeWeeks);

        // Assert
        assertEquals("The sum of 2 and 3 weeks should be 5", 5, sum.getWeeks());
    }

    @Test
    public void plus_isImmutable() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);
        final Weeks threeWeeks = Weeks.weeks(3);
        final String originalTwoWeeks = twoWeeks.toString();
        final String originalThreeWeeks = threeWeeks.toString();

        // Act
        twoWeeks.plus(threeWeeks);

        // Assert that the original instances were not modified
        assertEquals("Original Weeks(2) instance should not be modified", 2, twoWeeks.getWeeks());
        assertEquals("Original Weeks(3) instance should not be modified", 3, threeWeeks.getWeeks());
    }

    @Test
    public void plus_whenAddingNull_returnsSameInstance() {
        // Arrange
        final Weeks oneWeek = Weeks.ONE;

        // Act
        final Weeks result = oneWeek.plus((Weeks) null);

        // Assert that adding null has no effect and returns the same instance
        assertSame("Adding null should return the same instance", oneWeek, result);
    }
    
    @Test
    public void plus_whenAddingZero_returnsSameInstance() {
        // Arrange
        final Weeks oneWeek = Weeks.ONE;

        // Act
        final Weeks result = oneWeek.plus(Weeks.ZERO);

        // Assert that adding zero has no effect and returns the same instance
        assertSame("Adding zero should return the same instance", oneWeek, result);
    }

    @Test(expected = ArithmeticException.class)
    public void plus_whenResultOverflows_throwsArithmeticException() {
        // Act
        // This should throw an exception because the result exceeds Integer.MAX_VALUE.
        Weeks.MAX_VALUE.plus(Weeks.ONE);
    }
}