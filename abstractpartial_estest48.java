package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test suite verifies the behavior of the AbstractPartial class.
 * Note: The original class name "AbstractPartial_ESTestTest48" was preserved,
 * but a more descriptive name like "AbstractPartialTest" would be preferable.
 */
public class AbstractPartial_ESTestTest48 {

    /**
     * Tests that the equals() method is reflexive, meaning an object is always
     * equal to itself. This is a fundamental contract of the Object.equals() method.
     */
    @Test
    public void equals_shouldBeReflexive() {
        // Arrange: Create an instance of YearMonth, a concrete subclass of AbstractPartial.
        YearMonth yearMonth = new YearMonth();

        // Act & Assert: Verify that the object is equal to itself.
        // The assertEquals method internally uses the .equals() method for comparison.
        assertEquals("An object must be equal to itself.", yearMonth, yearMonth);
    }
}