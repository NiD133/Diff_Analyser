package org.joda.time;

import junit.framework.TestCase;

/**
 * Test case for the {@link Weeks#plus(int)} method.
 * 
 * This test suite verifies the behavior of adding an integer value to a Weeks object,
 * including standard addition, handling of edge cases like adding zero, and overflow conditions.
 */
public class WeeksTest extends TestCase {

    /**
     * Tests that adding a positive number of weeks results in a new Weeks
     * object with the correct value, and that the original object remains unchanged (is immutable).
     */
    public void testPlus_int_addsValueCorrectly() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);
        final int weeksToAdd = 3;
        final Weeks expectedResult = Weeks.weeks(5);

        // Act
        final Weeks actualResult = twoWeeks.plus(weeksToAdd);

        // Assert
        assertEquals("2 + 3 should result in 5 weeks", expectedResult, actualResult);
        assertEquals("Original Weeks object should be immutable", Weeks.weeks(2), twoWeeks);
    }

    /**
     * Tests that adding zero to a Weeks object is a no-op and returns the same instance.
     * This confirms an optimization where no new object is created.
     */
    public void testPlus_int_addingZeroReturnsSameInstance() {
        // Arrange
        final Weeks oneWeek = Weeks.ONE;

        // Act
        final Weeks result = oneWeek.plus(0);

        // Assert
        assertSame("Adding zero should return the same instance", oneWeek, result);
    }

    /**
     * Tests that adding a value that causes an integer overflow
     * correctly throws an ArithmeticException.
     */
    public void testPlus_int_throwsExceptionOnOverflow() {
        // Arrange
        final Weeks maxWeeks = Weeks.MAX_VALUE;
        final int weeksToAdd = 1;

        // Act & Assert
        try {
            maxWeeks.plus(weeksToAdd);
            fail("Expected an ArithmeticException to be thrown due to overflow");
        } catch (ArithmeticException ex) {
            // This is the expected behavior, so the test passes.
        }
    }
}