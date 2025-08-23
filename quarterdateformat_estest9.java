package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * A test suite for the equals() method of the QuarterDateFormat class.
 */
public class QuarterDateFormatEqualsTest {

    /**
     * Verifies that the equals() method returns false when comparing a
     * QuarterDateFormat instance with an object of a different,
     * incompatible type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange: Create a QuarterDateFormat instance and an object of a different type.
        QuarterDateFormat formatter = new QuarterDateFormat();
        Object otherObject = new Object();

        // Act: Compare the two objects for equality.
        boolean isEqual = formatter.equals(otherObject);

        // Assert: The result should be false.
        assertFalse("A QuarterDateFormat instance should not be equal to an object of a different type.", isEqual);
    }
}